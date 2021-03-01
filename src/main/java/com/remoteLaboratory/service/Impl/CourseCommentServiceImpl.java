package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.CourseComment;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.CourseCommentRepository;
import com.remoteLaboratory.repositories.CourseRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseCommentService;
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
 * 课程评论服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class CourseCommentServiceImpl implements CourseCommentService {
    private static Logger log = LoggerFactory.getLogger(CourseCommentServiceImpl.class);

    @Autowired
    private CourseCommentRepository courseCommentRepository;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public CourseComment add(CourseComment courseComment) throws BusinessException {
        CourseComment cc = this.courseCommentRepository.findByCourseIdAndUserId(courseComment.getCourseId(), courseComment.getUserId());
        if (cc != null) {
            throw new BusinessException(Messages.CODE_40010, "您已对该课程进行过评论！");
        }
        Course course = this.courseService.get(courseComment.getCourseId());
        courseComment = courseCommentRepository.save(courseComment);
        course.setCommentNumber(course.getCommentNumber() + 1);
        this.courseRepository.save(course);
        return courseComment;
    }

    @Override
    public CourseComment update(CourseComment courseComment) throws BusinessException {
        CourseComment courseCommentOld = this.courseCommentRepository.findOne(courseComment.getId());
        if(!courseCommentOld.getUserId().equals(courseComment.getUserId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        courseComment = courseCommentRepository.save(courseComment);
        return courseComment;
    }

    @Override
    public CourseComment setMainPageDisplay(CourseComment courseComment) throws BusinessException {
        this.courseCommentRepository.cancelMainPageDisplayByCourseId(courseComment.getCourseId());
        courseComment.setMainPageDisplay(true);
        courseComment = courseCommentRepository.save(courseComment);
        return courseComment;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            CourseComment courseComment = this.courseCommentRepository.findOne(id);
            if(courseComment != null) {
                Course course = this.courseService.get(courseComment.getCourseId());
                if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)
                        && !course.getTeacherId().equals(loginUser.getId())
                        && !courseComment.getUserId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                courseCommentRepository.delete(id);
                course.setCommentNumber(course.getCommentNumber() - 1);
                this.courseRepository.save(course);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("课程评论");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(courseComment.getId());
                logRecord.setObjectName(courseComment.getTitle());
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
            Page<CourseComment> list = courseCommentRepository.findAll(new MySpecification<CourseComment>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<CourseComment> list = courseCommentRepository.findAll(new MySpecification<CourseComment>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public CourseComment get(Integer id) throws BusinessException {
        CourseComment courseComment = courseCommentRepository.findOne(id);
        if (courseComment == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return courseComment;
    }

}
