package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.bo.SearchPara;
import com.remoteLaboratory.bo.SearchParas;
import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.UserRepository;
import com.remoteLaboratory.service.UserService;
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
 * 用户服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private LogRecordRepository logRecordRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, LogRecordRepository logRecordRepository) {
        this.userRepository = userRepository;
        this.logRecordRepository = logRecordRepository;
    }

    @Override
    public User add(User user) throws BusinessException {
        User oldUser = this.userRepository.findByUserName(user.getUserName());
        if(oldUser != null) {
            throw new BusinessException(Messages.CODE_40010, "用户名已存在！");
        }
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User update(User user) throws BusinessException {
        user = userRepository.save(user);
        return user;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            User user = this.userRepository.findOne(id);
            if(user != null) {
                userRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("用户");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(user.getId());
                logRecord.setObjectName(user.getUserName());
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
            Page<User> list = userRepository.findAll(new MySpecification<User>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<User> list = userRepository.findAll(new MySpecification<User>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public ListOutput listByUserType(ListInput listInput, String userType) throws BusinessException {
        SearchPara searchPara = new SearchPara();
        searchPara.setName("userType");
        searchPara.setOp("eq");
        searchPara.setValue(userType);
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
    public User get(Integer id) throws BusinessException {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return user;
    }

}
