package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.ReplyRepository;
import com.remoteLaboratory.repositories.SubjectRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.ReplyService;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 回复服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class ReplyServiceImpl implements ReplyService {
    private static Logger log = LoggerFactory.getLogger(ReplyServiceImpl.class);

    private ReplyRepository replyRepository;

    private LogRecordRepository logRecordRepository;

    private SubjectRepository subjectRepository;

    private CourseService courseService;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository,
                            LogRecordRepository logRecordRepository,
                            SubjectRepository subjectRepository,
                            JdbcTemplate jdbcTemplate,
                            CourseService courseService) {
        this.replyRepository = replyRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
        this.subjectRepository = subjectRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reply add(Reply reply) throws BusinessException {
        String sql = "select max(rr.floor_number) number from rl_reply rr where rr.subject_id = " + reply.getSubjectId();
        Map<String, Object> result = this.jdbcTemplate.queryForMap(sql);
        Integer floorNumber = 1;
        if(result.get("number") != null) {
            floorNumber = (Integer) result.get("number") + 1;
        }
        reply.setFloorNumber(floorNumber);
        reply = this.replyRepository.save(reply);
        Subject subject = this.subjectRepository.findOne(reply.getSubjectId());
        subject.setReplyNumber(subject.getReplyNumber() + 1);
        subject = this.subjectRepository.save(subject);
        if(reply.getType().equals(2)) {
            Reply pReply = this.replyRepository.findOne(reply.getReplayId());
            pReply.setReplyNumber(pReply.getReplyNumber());
            pReply = this.replyRepository.save(pReply);
        }
        return reply;
    }

    @Override
    public Reply update(Reply reply) throws BusinessException {
        reply = this.replyRepository.save(reply);
        return reply;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            Reply reply = this.replyRepository.findOne(id);
            if(reply != null) {
                Subject subject = this.subjectRepository.findOne(reply.getSubjectId());
                Course course = this.courseService.get(subject.getCourseId());
                if(!reply.getUserId().equals(loginUser.getId())
                        && !loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)
                        && !course.getTeacherId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                this.replyRepository.delete(id);
                this.replyRepository.deleteByReplayId(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("回复");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(reply.getId());
                logRecord.setObjectName(reply.getSubjectTitle());
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
            Page<Reply> list = this.replyRepository.findAll(new MySpecification<Reply>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<Reply> list = this.replyRepository.findAll(new MySpecification<Reply>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public Reply get(Integer id) throws BusinessException {
        Reply reply = this.replyRepository.findOne(id);
        if (reply == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return reply;
    }

}
