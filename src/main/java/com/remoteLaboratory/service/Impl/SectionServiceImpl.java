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

    private CourseStudyRecordService courseStudyRecordService;

    private LogRecordRepository logRecordRepository;

    private CourseService courseService;

    @Autowired
    public SectionServiceImpl(SectionRepository sectionRepository,
                              CourseService courseService,
                              SectionStudyRecordRepository sectionStudyRecordRepository,
                              CourseStudyRecordService courseStudyRecordService,
                              CourseStudyRecordRepository courseStudyRecordRepository,
                              LogRecordRepository logRecordRepository) {
        this.sectionRepository = sectionRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
        this.sectionStudyRecordRepository = sectionStudyRecordRepository;
        this.courseStudyRecordService = courseStudyRecordService;
        this.courseStudyRecordRepository = courseStudyRecordRepository;
    }

    @Override
    public Section add(Section section) throws BusinessException {
        section = sectionRepository.save(section);
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
            for(Section section : list.getContent()) {
                section.setContent(null);
            }
            listOutput.setList(list.getContent());
        } else {
            List<Section> list = sectionRepository.findAll(new MySpecification<Section>(listInput.getSearchParas()));
            for(Section section : list) {
                section.setContent(null);
            }
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
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
                sectionStudyRecord.setStudied(sectionStudyRecord.getStudied() + 0.5);
                sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
                this.courseStudyRecordService.update(sectionStudyRecord.getCourseStudyRecordId());
            }
        }
        else {
            this.courseStudyRecordService.startStudy(section.getCourseId(), user);
            sectionStudyRecord = this.sectionStudyRecordRepository.findBySectionIdAndUserId(id, user.getId());
            sectionStudyRecord.setStudyStatus(1); // 状态设置为已学习
            sectionStudyRecord.setStudied(sectionStudyRecord.getStudied() + 0.5);
            sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
            this.courseStudyRecordService.update(sectionStudyRecord.getCourseStudyRecordId());
        }
        return sectionStudyRecord;
    }

}
