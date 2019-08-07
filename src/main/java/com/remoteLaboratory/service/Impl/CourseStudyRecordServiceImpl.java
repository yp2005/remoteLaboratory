package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.bo.SearchPara;
import com.remoteLaboratory.bo.SearchParas;
import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.*;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.CourseStudyRecordService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ChapterStudyRecordPublicVo;
import com.remoteLaboratory.vo.CourseStudyRecordPublicVo;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
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
 * 课程学习记录服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class CourseStudyRecordServiceImpl implements CourseStudyRecordService {
    private static Logger log = LoggerFactory.getLogger(CourseStudyRecordServiceImpl.class);

    private CourseStudyRecordRepository courseStudyRecordRepository;

    private LogRecordRepository logRecordRepository;

    private CourseService courseService;

    private CourseRepository courseRepository;

    private ChapterRepository chapterRepository;

    private SectionRepository sectionRepository;

    private ChapterStudyRecordRepository chapterStudyRecordRepository;

    private SectionStudyRecordRepository sectionStudyRecordRepository;

    @Autowired
    public CourseStudyRecordServiceImpl(CourseStudyRecordRepository courseStudyRecordRepository,
                                        LogRecordRepository logRecordRepository,
                                        ChapterRepository chapterRepository,
                                        SectionRepository sectionRepository,
                                        ChapterStudyRecordRepository chapterStudyRecordRepository,
                                        SectionStudyRecordRepository sectionStudyRecordRepository,
                                        CourseRepository courseRepository,
                                        CourseService courseService) {
        this.courseStudyRecordRepository = courseStudyRecordRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
        this.chapterRepository = chapterRepository;
        this.sectionRepository = sectionRepository;
        this.chapterStudyRecordRepository = chapterStudyRecordRepository;
        this.sectionStudyRecordRepository = sectionStudyRecordRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseStudyRecord add(CourseStudyRecord courseStudyRecord) throws BusinessException {
        courseStudyRecord = courseStudyRecordRepository.save(courseStudyRecord);
        return courseStudyRecord;
    }

    @Override
    public CourseStudyRecordPublicVo startStudy(Integer courseId, User user) throws BusinessException {
        CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findByCourseIdAndUserId(courseId, user.getId());
        if(courseStudyRecord == null) {
            Course course = this.courseService.get(courseId);
            if(!course.getStatus().equals(1)) {
                throw new BusinessException(Messages.CODE_40010, "课程已结束或尚未开始");
            }
            courseStudyRecord = new CourseStudyRecord();
            courseStudyRecord.setStudied(0.0);
            courseStudyRecord.setScore(0.0);
            courseStudyRecord.setUserId(user.getId());
            courseStudyRecord.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
            courseStudyRecord.setCourseId(course.getId());
            courseStudyRecord.setCourseName(course.getName());
            courseStudyRecord.setCourseMainImg(course.getMainImg());
            courseStudyRecord.setCourseIntroduction(course.getIntroduction());
            courseStudyRecord = this.courseStudyRecordRepository.save(courseStudyRecord);
            CourseStudyRecordPublicVo courseStudyRecordPublicVo = new CourseStudyRecordPublicVo(courseStudyRecord);
            List<ChapterStudyRecordPublicVo> chapterStudyRecordPublicVoList = new ArrayList<>();
            List<Chapter> chapterList = this.chapterRepository.findByCourseId(courseId);
            if(CollectionUtils.isNotEmpty(chapterList)) {
                for(Chapter chapter : chapterList) {
                    ChapterStudyRecord chapterStudyRecord = new ChapterStudyRecord();
                    chapterStudyRecord.setChapterId(chapter.getId());
                    chapterStudyRecord.setChapterName(chapter.getName());
                    chapterStudyRecord.setChapterTitle(chapter.getTitle());
                    chapterStudyRecord.setCourseStudyRecordId(courseStudyRecord.getId());
                    chapterStudyRecord.setStudied(0.0);
                    chapterStudyRecord.setUserId(user.getId());
                    chapterStudyRecord.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
                    chapterStudyRecord = this.chapterStudyRecordRepository.save(chapterStudyRecord);
                    ChapterStudyRecordPublicVo chapterStudyRecordPublicVo = new ChapterStudyRecordPublicVo(chapterStudyRecord);
                    List<SectionStudyRecord> sectionStudyRecordList = new ArrayList<>();
                    List<Section> sectionList = this.sectionRepository.findByChapterId(chapter.getId());
                    if(CollectionUtils.isNotEmpty(sectionList)) {
                        for(Section section : sectionList) {
                            SectionStudyRecord sectionStudyRecord = new SectionStudyRecord();
                            sectionStudyRecord.setChapterStudyRecordId(chapterStudyRecord.getId());
                            sectionStudyRecord.setCourseStudyRecordId(courseStudyRecord.getId());
                            sectionStudyRecord.setChapterId(chapter.getId());
                            sectionStudyRecord.setChapterName(chapter.getName());
                            sectionStudyRecord.setChapterTitle(chapter.getTitle());
                            sectionStudyRecord.setSectionId(section.getId());
                            sectionStudyRecord.setSectionName(section.getName());
                            sectionStudyRecord.setSectionTitle(section.getTitle());
                            sectionStudyRecord.setStudied(0.0);
                            sectionStudyRecord.setStudyStatus(0);
                            sectionStudyRecord.setTestStatus(0);
                            sectionStudyRecord.setUserId(user.getId());
                            sectionStudyRecord.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
                            sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
                            sectionStudyRecordList.add(sectionStudyRecord);
                        }
                    }
                    chapterStudyRecordPublicVo.setSectionStudyRecordList(sectionStudyRecordList);
                    chapterStudyRecordPublicVoList.add(chapterStudyRecordPublicVo);
                }
            }
            courseStudyRecordPublicVo.setChapterStudyRecordPublicVoList(chapterStudyRecordPublicVoList);
            course.setStudentNumber(course.getStudentNumber() + 1);
            course = this.courseRepository.save(course);
            return courseStudyRecordPublicVo;
        }
        else {
            return this.getDetailByCourseId(courseId, user.getId());
        }
    }

    @Override
    public void update(Integer id) throws BusinessException {
        CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findOne(id);
        if (courseStudyRecord == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        List<ChapterStudyRecord> chapterStudyRecordList = this.chapterStudyRecordRepository.findByCourseStudyRecordId(courseStudyRecord.getId());
        Double courseStudied = 0.0;
        if(CollectionUtils.isNotEmpty(chapterStudyRecordList)) {
            Double chapterPercent = 1.0 / chapterStudyRecordList.size();
            for(ChapterStudyRecord chapterStudyRecord : chapterStudyRecordList) {
                List<SectionStudyRecord> sectionStudyRecordList = this.sectionStudyRecordRepository.findByChapterStudyRecordId(chapterStudyRecord.getId());
                Double chapterStudied = 0.0;
                if(CollectionUtils.isNotEmpty(sectionStudyRecordList)) {
                    Double sectionPercent = 1.0 / sectionStudyRecordList.size();
                    for(SectionStudyRecord sectionStudyRecord : sectionStudyRecordList) {
                        chapterStudied += sectionPercent * sectionStudyRecord.getStudied();
                    }
                }
                else {
                    chapterStudied = 1.0;
                }
                chapterStudied = Math.round(chapterStudied * 100) / 100.0;
                chapterStudyRecord.setStudied(chapterStudied);
                chapterStudyRecord = this.chapterStudyRecordRepository.save(chapterStudyRecord);
                courseStudied += chapterPercent * chapterStudied;
            }
        }
        else {
            courseStudied = 1.0;
        }
        courseStudied = Math.round(courseStudied * 100) / 100.0;
        courseStudyRecord.setStudied(courseStudied);
        if(courseStudied.equals(1.0)) { // TODO 计算课程总分

        }
        courseStudyRecord.setStatus(courseStudyRecord.getStudied().equals(1.0) ? 1 : 0);
        courseStudyRecord = this.courseStudyRecordRepository.save(courseStudyRecord);
    }

    @Override
    public CourseStudyRecord update(CourseStudyRecord courseStudyRecord) throws BusinessException {
        courseStudyRecord = courseStudyRecordRepository.save(courseStudyRecord);
        return courseStudyRecord;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findOne(id);
            if(courseStudyRecord != null) {
                Course course = this.courseService.get(courseStudyRecord.getCourseId());
                if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                courseStudyRecordRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("课程学习记录");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(courseStudyRecord.getId());
                logRecord.setObjectName(courseStudyRecord.getUserName() + ": " + courseStudyRecord.getCourseName());
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
            Page<CourseStudyRecord> list = courseStudyRecordRepository.findAll(new MySpecification<CourseStudyRecord>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<CourseStudyRecord> list = courseStudyRecordRepository.findAll(new MySpecification<CourseStudyRecord>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public ListOutput listByCourseId(ListInput listInput, Integer courseId) throws BusinessException {
        SearchPara searchPara = new SearchPara();
        searchPara.setName("courseId");
        searchPara.setOp("eq");
        searchPara.setValue(courseId);
        if (listInput.getSearchParas() == null) {
            listInput.setSearchParas(new SearchParas());
        }
        if (listInput.getSearchParas().getConditions() == null) {
            listInput.getSearchParas().setConditions(new ArrayList<>());
        }
        listInput.getSearchParas().getConditions().add(searchPara);
        return this.list(listInput);
    }

    @Override
    public ListOutput listByUserId(ListInput listInput, Integer userId) throws BusinessException {
        SearchPara searchPara = new SearchPara();
        searchPara.setName("userId");
        searchPara.setOp("eq");
        searchPara.setValue(userId);
        if (listInput.getSearchParas() == null) {
            listInput.setSearchParas(new SearchParas());
        }
        if (listInput.getSearchParas().getConditions() == null) {
            listInput.getSearchParas().setConditions(new ArrayList<>());
        }
        listInput.getSearchParas().getConditions().add(searchPara);
        return this.list(listInput);
    }

    @Override
    public CourseStudyRecord get(Integer id) throws BusinessException {
        CourseStudyRecord courseStudyRecord = courseStudyRecordRepository.findOne(id);
        if (courseStudyRecord == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return courseStudyRecord;
    }

    @Override
    public CourseStudyRecordPublicVo getDetailByCourseId(Integer courseId, Integer userId) throws BusinessException {
        CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findByCourseIdAndUserId(courseId, userId);
        if (courseStudyRecord == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        CourseStudyRecordPublicVo courseStudyRecordPublicVo = new CourseStudyRecordPublicVo(courseStudyRecord);
        List<ChapterStudyRecordPublicVo> chapterStudyRecordPublicVoList = new ArrayList<>();
        List<ChapterStudyRecord> chapterStudyRecordList = this.chapterStudyRecordRepository.findByCourseStudyRecordId(courseStudyRecord.getId());
        if(CollectionUtils.isNotEmpty(chapterStudyRecordList)) {
            for(ChapterStudyRecord chapterStudyRecord : chapterStudyRecordList) {
                ChapterStudyRecordPublicVo chapterStudyRecordPublicVo = new ChapterStudyRecordPublicVo(chapterStudyRecord);
                List<SectionStudyRecord> sectionStudyRecordList = this.sectionStudyRecordRepository.findByChapterStudyRecordId(chapterStudyRecord.getId());
                chapterStudyRecordPublicVo.setSectionStudyRecordList(sectionStudyRecordList);
                chapterStudyRecordPublicVoList.add(chapterStudyRecordPublicVo);
            }
        }
        courseStudyRecordPublicVo.setChapterStudyRecordPublicVoList(chapterStudyRecordPublicVoList);
        return courseStudyRecordPublicVo;
    }

    @Override
    public CourseStudyRecordPublicVo getDetailById(Integer id) throws BusinessException {
        CourseStudyRecord courseStudyRecord = courseStudyRecordRepository.findOne(id);
        if (courseStudyRecord == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        CourseStudyRecordPublicVo courseStudyRecordPublicVo = new CourseStudyRecordPublicVo(courseStudyRecord);
        List<ChapterStudyRecordPublicVo> chapterStudyRecordPublicVoList = new ArrayList<>();
        List<ChapterStudyRecord> chapterStudyRecordList = this.chapterStudyRecordRepository.findByCourseStudyRecordId(courseStudyRecord.getId());
        if(CollectionUtils.isNotEmpty(chapterStudyRecordList)) {
            for(ChapterStudyRecord chapterStudyRecord : chapterStudyRecordList) {
                ChapterStudyRecordPublicVo chapterStudyRecordPublicVo = new ChapterStudyRecordPublicVo(chapterStudyRecord);
                List<SectionStudyRecord> sectionStudyRecordList = this.sectionStudyRecordRepository.findByChapterStudyRecordId(chapterStudyRecord.getId());
                chapterStudyRecordPublicVo.setSectionStudyRecordList(sectionStudyRecordList);
                chapterStudyRecordPublicVoList.add(chapterStudyRecordPublicVo);
            }
        }
        courseStudyRecordPublicVo.setChapterStudyRecordPublicVoList(chapterStudyRecordPublicVoList);
        return courseStudyRecordPublicVo;
    }

}
