package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.CourseRepository;
import com.remoteLaboratory.repositories.ReplyRepository;
import com.remoteLaboratory.repositories.SubjectRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.SubjectService;
import com.remoteLaboratory.service.CourseService;
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
 * 话题服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {
    private static Logger log = LoggerFactory.getLogger(SubjectServiceImpl.class);

    private SubjectRepository subjectRepository;

    private ReplyRepository replyRepository;

    private LogRecordRepository logRecordRepository;

    private CourseService courseService;

    private CourseRepository courseRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository,
                              LogRecordRepository logRecordRepository,
                              ReplyRepository replyRepository,
                              CourseRepository courseRepository,
                              CourseService courseService) {
        this.subjectRepository = subjectRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.replyRepository = replyRepository;
    }

    @Override
    public Subject add(Subject subject) throws BusinessException {
        subject = subjectRepository.save(subject);
        Course course = this.courseService.get(subject.getCourseId());
        course.setSubjectNumber(course.getSubjectNumber() + 1);
        course = this.courseRepository.save(course);
        return subject;
    }

    @Override
    public Subject update(Subject subject) throws BusinessException {
        subject = subjectRepository.save(subject);
        return subject;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            Subject subject = this.subjectRepository.findOne(id);
            if(subject != null) {
                Course course = this.courseService.get(subject.getCourseId());
                if(!subject.getUserId().equals(loginUser.getId())
                        && !loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)
                        && !course.getTeacherId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                subjectRepository.delete(id);
                course.setSubjectNumber(course.getSubjectNumber() - 1);
                this.courseRepository.save(course);
                this.replyRepository.deleteBySubjectId(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("话题");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(subject.getId());
                logRecord.setObjectName(subject.getTitle());
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
            Page<Subject> list = subjectRepository.findAll(new MySpecification<Subject>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<Subject> list = subjectRepository.findAll(new MySpecification<Subject>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public Subject get(Integer id) throws BusinessException {
        Subject subject = subjectRepository.findOne(id);
        if (subject == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return subject;
    }

}
