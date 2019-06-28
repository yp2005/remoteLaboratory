package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.UserOnlineTime;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.UserOnlineTimeRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.UserOnlineTimeService;
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
 * 用户在线时间服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class UserOnlineTimeServiceImpl implements UserOnlineTimeService {
    private static Logger log = LoggerFactory.getLogger(UserOnlineTimeServiceImpl.class);

    private UserOnlineTimeRepository userOnlineTimeRepository;

    private LogRecordRepository logRecordRepository;

    private CourseService courseService;

    @Autowired
    public UserOnlineTimeServiceImpl(UserOnlineTimeRepository userOnlineTimeRepository, LogRecordRepository logRecordRepository, CourseService courseService) {
        this.userOnlineTimeRepository = userOnlineTimeRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
    }

    @Override
    public UserOnlineTime add(UserOnlineTime userOnlineTime) throws BusinessException {
        userOnlineTime = userOnlineTimeRepository.save(userOnlineTime);
        return userOnlineTime;
    }

    @Override
    public UserOnlineTime update(UserOnlineTime userOnlineTime) throws BusinessException {
        userOnlineTime = userOnlineTimeRepository.save(userOnlineTime);
        return userOnlineTime;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            UserOnlineTime userOnlineTime = this.userOnlineTimeRepository.findOne(id);
            if(userOnlineTime != null) {
                if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                userOnlineTimeRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("用户在线时间");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(userOnlineTime.getId());
                logRecord.setObjectName(userOnlineTime.getUserName());
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
            Page<UserOnlineTime> list = userOnlineTimeRepository.findAll(new MySpecification<UserOnlineTime>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<UserOnlineTime> list = userOnlineTimeRepository.findAll(new MySpecification<UserOnlineTime>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public UserOnlineTime get(Integer id) throws BusinessException {
        UserOnlineTime userOnlineTime = userOnlineTimeRepository.findOne(id);
        if (userOnlineTime == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return userOnlineTime;
    }

}
