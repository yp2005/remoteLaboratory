package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.*;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.CourseStudyRecordService;
import com.remoteLaboratory.service.SectionService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import com.remoteLaboratory.vo.SectionOutput;
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
 * 课程节服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class SectionServiceImpl implements SectionService {
    private static Logger log = LoggerFactory.getLogger(SectionServiceImpl.class);

    private SectionRepository sectionRepository;

    private SectionStudyRecordRepository sectionStudyRecordRepository;

    private CourseStudyRecordRepository courseStudyRecordRepository;

    private ChapterStudyRecordRepository chapterStudyRecordRepository;

    private CourseStudyRecordService courseStudyRecordService;

    private LogRecordRepository logRecordRepository;

    private CourseService courseService;

    @Autowired
    public SectionServiceImpl(SectionRepository sectionRepository,
                              CourseService courseService,
                              SectionStudyRecordRepository sectionStudyRecordRepository,
                              CourseStudyRecordService courseStudyRecordService,
                              ChapterStudyRecordRepository chapterStudyRecordRepository,
                              CourseStudyRecordRepository courseStudyRecordRepository,
                              LogRecordRepository logRecordRepository) {
        this.sectionRepository = sectionRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
        this.sectionStudyRecordRepository = sectionStudyRecordRepository;
        this.courseStudyRecordService = courseStudyRecordService;
        this.courseStudyRecordRepository = courseStudyRecordRepository;
        this.chapterStudyRecordRepository = chapterStudyRecordRepository;
    }

    @Override
    public Section add(Section section) throws BusinessException {
        section = sectionRepository.save(section);
        List<CourseStudyRecord> courseStudyRecordList = this.courseStudyRecordRepository.findByCourseIdAndStatus(section.getCourseId(), 0);
        if(CollectionUtils.isNotEmpty(courseStudyRecordList)) {
            for(CourseStudyRecord courseStudyRecord : courseStudyRecordList) {
                ChapterStudyRecord chapterStudyRecord = this.chapterStudyRecordRepository.findByCourseStudyRecordIdAndChapterId(courseStudyRecord.getId(), section.getChapterId());
                if(chapterStudyRecord == null) {
                    chapterStudyRecord = new ChapterStudyRecord();
                    chapterStudyRecord.setChapterId(section.getChapterId());
                    chapterStudyRecord.setChapterName(section.getChapterName());
                    chapterStudyRecord.setChapterTitle(section.getChapterTitle());
                    chapterStudyRecord.setCourseStudyRecordId(courseStudyRecord.getId());
                    chapterStudyRecord.setStudied(0.0);
                    chapterStudyRecord.setUserId(courseStudyRecord.getUserId());
                    chapterStudyRecord.setUserName(courseStudyRecord.getUserName());
                    chapterStudyRecord = this.chapterStudyRecordRepository.save(chapterStudyRecord);
                }
                SectionStudyRecord sectionStudyRecord = new SectionStudyRecord();
                sectionStudyRecord.setChapterStudyRecordId(chapterStudyRecord.getId());
                sectionStudyRecord.setCourseStudyRecordId(courseStudyRecord.getId());
                sectionStudyRecord.setChapterId(chapterStudyRecord.getChapterId());
                sectionStudyRecord.setChapterName(chapterStudyRecord.getChapterName());
                sectionStudyRecord.setChapterTitle(chapterStudyRecord.getChapterTitle());
                sectionStudyRecord.setSectionId(section.getId());
                sectionStudyRecord.setSectionName(section.getName());
                sectionStudyRecord.setSectionTitle(section.getTitle());
                sectionStudyRecord.setStudyStatus(0);
                sectionStudyRecord.setUserId(chapterStudyRecord.getUserId());
                sectionStudyRecord.setUserName(chapterStudyRecord.getUserName());
                sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
                this.courseStudyRecordService.updatePercent(courseStudyRecord);
            }
        }
        return section;
    }

    @Override
    public Section update(Section section) throws BusinessException {
        section = sectionRepository.save(section);
        return section;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            Section section = this.sectionRepository.findOne(id);
            if(section != null) {
                Course course = this.courseService.get(section.getCourseId());
                if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                if(course.getStatus().equals(1)) {
                    throw new BusinessException(Messages.CODE_40010, "课程进行中不能执行删除操作");
                }
                sectionRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("课程节");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(section.getId());
                logRecord.setObjectName(section.getTitle());
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
            Page<Section> list = sectionRepository.findAll(new MySpecification<Section>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            List<SectionOutput> sectionOutputList = new ArrayList<>();
            for(Section section : list.getContent()) {
                sectionOutputList.add(new SectionOutput(section));
            }
            listOutput.setList(sectionOutputList);
        } else {
            List<Section> list = sectionRepository.findAll(new MySpecification<Section>(listInput.getSearchParas()));
            List<SectionOutput> sectionOutputList = new ArrayList<>();
            for(Section section : list) {
                sectionOutputList.add(new SectionOutput(section));
            }
            listOutput.setTotalNum(list.size());
            listOutput.setList(sectionOutputList);
        }
        return listOutput;
    }

    @Override
    public Section get(Integer id) throws BusinessException {
        Section section = sectionRepository.findOne(id);
        if (section == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return section;
    }

    @Override
    public Section startStudy(Integer id, User user) throws BusinessException {
        Section section = sectionRepository.findOne(id);
        if (section == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        SectionStudyRecord sectionStudyRecord = this.sectionStudyRecordRepository.findBySectionIdAndUserId(id, user.getId());
        if(sectionStudyRecord != null) {
            if(sectionStudyRecord.getStudyStatus().equals(0)) {
                sectionStudyRecord.setStudyStatus(2); // 状态设置为正在学习
                sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
            }
        }
        else {
            this.courseStudyRecordService.startStudy(section.getCourseId(), user);
            sectionStudyRecord = this.sectionStudyRecordRepository.findBySectionIdAndUserId(id, user.getId());
            sectionStudyRecord.setStudyStatus(2); // 状态设置为正在学习
            sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
        }
        CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findOne(sectionStudyRecord.getCourseStudyRecordId());
        courseStudyRecord.setChapterId(sectionStudyRecord.getChapterId());
        courseStudyRecord.setChapterName(sectionStudyRecord.getChapterName());
        courseStudyRecord.setChapterTitle(sectionStudyRecord.getChapterTitle());
        courseStudyRecord.setSectionId(section.getId());
        courseStudyRecord.setSectionName(section.getName());
        courseStudyRecord.setSectionTitle(section.getTitle());
        this.courseStudyRecordRepository.save(courseStudyRecord);
        return section;
    }

    @Override
    public SectionStudyRecord finish(Integer id, User user) throws BusinessException {
        Section section = sectionRepository.findOne(id);
        if (section == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        SectionStudyRecord sectionStudyRecord = this.sectionStudyRecordRepository.findBySectionIdAndUserId(id, user.getId());
        if(sectionStudyRecord != null) {
            if(!sectionStudyRecord.getStudyStatus().equals(1)) {
                sectionStudyRecord.setStudyStatus(1); // 状态设置为已学习
                sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
                this.courseStudyRecordService.updatePercent(this.courseStudyRecordRepository.findOne(sectionStudyRecord.getCourseStudyRecordId()));
            }
        }
        else {
            this.courseStudyRecordService.startStudy(section.getCourseId(), user);
            sectionStudyRecord = this.sectionStudyRecordRepository.findBySectionIdAndUserId(id, user.getId());
            sectionStudyRecord.setStudyStatus(1); // 状态设置为已学习
            sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
            this.courseStudyRecordService.updatePercent(this.courseStudyRecordRepository.findOne(sectionStudyRecord.getCourseStudyRecordId()));
        }
        return sectionStudyRecord;
    }

}
