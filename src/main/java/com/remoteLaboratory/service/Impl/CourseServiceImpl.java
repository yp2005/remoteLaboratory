package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.CourseRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseService;
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
 * 课程服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    private static Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private CourseRepository courseRepository;

    private LogRecordRepository logRecordRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             LogRecordRepository logRecordRepository) {
        this.courseRepository = courseRepository;
        this.logRecordRepository = logRecordRepository;
    }

    @Override
    public Course add(Course course) throws BusinessException {
        course = courseRepository.save(course);
        return course;
    }

    @Override
    public Course update(Course course) throws BusinessException {
        course = courseRepository.save(course);
        return course;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            Course course = courseRepository.findOne(id);
            if (course != null) {
                courseRepository.delete(id);
                // TODO 删除关联的各种对象
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
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
}
