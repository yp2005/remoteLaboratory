package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.SignalChannel;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.SignalChannelRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.SignalChannelService;
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
 * 信号通道服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class SignalChannelServiceImpl implements SignalChannelService {
    private static Logger log = LoggerFactory.getLogger(SignalChannelServiceImpl.class);

    private SignalChannelRepository signalChannelRepository;

    private LogRecordRepository logRecordRepository;

    @Autowired
    public SignalChannelServiceImpl(SignalChannelRepository signalChannelRepository, LogRecordRepository logRecordRepository) {
        this.signalChannelRepository = signalChannelRepository;
        this.logRecordRepository = logRecordRepository;
    }

    @Override
    public SignalChannel add(SignalChannel signalChannel) throws BusinessException {
        signalChannel = signalChannelRepository.save(signalChannel);
        return signalChannel;
    }

    @Override
    public SignalChannel update(SignalChannel signalChannel) throws BusinessException {
        signalChannel = signalChannelRepository.save(signalChannel);
        return signalChannel;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            SignalChannel signalChannel = this.signalChannelRepository.findOne(id);
            if(signalChannel != null) {
                if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                signalChannelRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("信号通道");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(signalChannel.getId());
                logRecord.setObjectName(signalChannel.getName());
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
            Page<SignalChannel> list = signalChannelRepository.findAll(new MySpecification<SignalChannel>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<SignalChannel> list = signalChannelRepository.findAll(new MySpecification<SignalChannel>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public SignalChannel get(Integer id) throws BusinessException {
        SignalChannel signalChannel = signalChannelRepository.findOne(id);
        if (signalChannel == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return signalChannel;
    }

}
