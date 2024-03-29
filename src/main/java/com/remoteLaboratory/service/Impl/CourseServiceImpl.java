package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.*;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.CourseStudyRecordService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    private static Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private CourseStudyRecordRepository courseStudyRecordRepository;

    @Autowired
    private CourseStudyRecordService courseStudyRecordService;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TestInstanceRepository testInstanceRepository;

    @Override
    public Course add(Course course) throws BusinessException {
        course = courseRepository.save(course);
        return course;
    }

    @Override
    public Course update(Course course) throws BusinessException {
        Course courseOld = this.get(course.getId());
        course.setStudentNumber(courseOld.getStudentNumber());
        course.setSubjectNumber(courseOld.getSubjectNumber());
        course.setCommentNumber(courseOld.getCommentNumber());
        course.setPreStudyScore(courseOld.getPreStudyScore());
        course.setReportScore(courseOld.getReportScore());
        course = courseRepository.save(course);
        this.courseStudyRecordRepository.updateByCourseId(course.getId(), course.getName(), course.getMainImg(), course.getIntroduction());
        return course;
    }

    @Override
    public Course updateStatus(Integer courseId, Integer status, User loginUser) throws BusinessException {
        Course course = this.get(courseId);
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(status.equals(course.getStatus())) {
            throw new BusinessException(Messages.CODE_40001);
        }
        if((course.getStatus().equals(1) && status.equals(0))
                || (course.getStatus().equals(0) && status.equals(2))) {
            throw new BusinessException(Messages.CODE_40001);
        }
        if(status.equals(2)) { // 结束课程
            List<CourseStudyRecord> courseStudyRecordList = this.courseStudyRecordRepository.findByCourseIdAndStatus(courseId, 0);
            if(courseStudyRecordList.size() > 0) { // 处理未完成学习的学记录
                for (CourseStudyRecord courseStudyRecord : courseStudyRecordList) {
                    courseStudyRecord.setStatus(1);
                    if(!courseStudyRecord.getGraded()) {
                        courseStudyRecord = this.courseStudyRecordService.calculateScore(courseStudyRecord);
                    }
                    courseStudyRecord = this.courseStudyRecordRepository.save(courseStudyRecord);
                }
            }
            // 结束课程的同时结束实验
            course.setExperimentStarted(false);
        }
        else if(status.equals(1)) { // 开始课程，将学习记录、实验报告设置为往期课程
            if(course.getReportScore() == null || course.getPreStudyScore() == null) {
                throw new BusinessException(Messages.CODE_40010, "请完成课程分数设置再开始课程！");
            }
            this.courseStudyRecordRepository.updateOldByCourseId(courseId);
            this.testInstanceRepository.updateOldByCourseId(courseId);
        }
        course.setStatus(status);
        course = courseRepository.save(course);
        return course;
    }

    @Override
    public Course setScore(SetScoreInput setScoreInput, User loginUser) throws BusinessException {
        Course course = this.get(setScoreInput.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(course.getStatus().equals(1)) {
            throw new BusinessException(Messages.CODE_40010, "课程进行中，不能设置分数分布！");
        }
        if(setScoreInput.getPreStudyScore() + setScoreInput.getReportScore() != 100) {
            throw new BusinessException(Messages.CODE_40010, "课程总分为100分！");
        }
        course.setPreStudyScore(setScoreInput.getPreStudyScore());
        course.setReportScore(setScoreInput.getReportScore());
        return courseRepository.save(course);
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            Course course = courseRepository.findOne(id);
            if (course != null) {
                if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                if(course.getStatus().equals(1)) {
                    throw new BusinessException(Messages.CODE_40010, "课程进行中不能执行删除操作");
                }
                course.setIsDeleted(true);
                this.courseRepository.save(course);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("逻辑删除");
                logRecord.setObject("课程");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(course.getId());
                logRecord.setObjectName(course.getName());
                logRecords.add(logRecord);
            }
        }
        for(LogRecord logRecord : logRecords) {
            logRecordRepository.save(logRecord);
        }
    }

    @Override
    public ListOutput list(ListInput listInput) throws BusinessException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if (StringUtils.isNotBlank(listInput.getSortDirection())
                && StringUtils.isNotBlank(listInput.getSortProperties())) {
            sort = new Sort(Sort.Direction.fromString(listInput.getSortDirection()), listInput.getSortProperties());
        }
        Pageable pageable = null;
        if (listInput.getPage() != null && listInput.getPageSize() != null) {
            pageable = new PageRequest(listInput.getPage(), listInput.getPageSize(), sort);
        }
        ListOutput listOutput = new ListOutput();
        if (pageable != null) {
            Page<Course> list = courseRepository.findAll(new MySpecification<Course>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<Course> list = courseRepository.findAll(new MySpecification<Course>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public Course get(Integer id) throws BusinessException {
        Course course = courseRepository.findOne(id);
        if (course == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return course;
    }

    @Override
    public CoursePublicVo getDetail(Integer id) throws BusinessException {
        Course course = courseRepository.findOne(id);
        if (course == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        CoursePublicVo coursePublicVo = new CoursePublicVo(course);
        List<Chapter> chapterList = this.chapterRepository.findByCourseId(course.getId());
        if(CollectionUtils.isNotEmpty(chapterList)) {
            List<ChapterPublicVo> chapterPublicVoList = new ArrayList<>();
            for(Chapter chapter : chapterList) {
                ChapterPublicVo chapterPublicVo = new ChapterPublicVo(chapter);
                List<Section> sectionList = this.sectionRepository.findByChapterId(chapter.getId());
                if(CollectionUtils.isNotEmpty(chapterList)) {
                    List<SectionOutput> sectionOutputList = new ArrayList<>();
                    for(Section section : sectionList) {
                        SectionOutput sectionOutput = new SectionOutput(section);
                        sectionOutputList.add(sectionOutput);
                    }
                    chapterPublicVo.setSectionList(sectionOutputList);
                }
                chapterPublicVoList.add(chapterPublicVo);
            }
            coursePublicVo.setChapterList(chapterPublicVoList);
        }
        return coursePublicVo;
    }
}
