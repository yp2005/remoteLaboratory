package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.LogRecordService;
import com.remoteLaboratory.utils.LogUtil;
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
import java.util.Date;
import java.util.List;

/**
 * 日志记录服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class LogRecordServiceImpl implements LogRecordService {
    private static Logger log = LoggerFactory.getLogger(LogRecordServiceImpl.class);

    private LogRecordRepository logRecordRepository;

    @Autowired
    public LogRecordServiceImpl(LogRecordRepository logRecordRepository) {
        this.logRecordRepository = logRecordRepository;
    }

    @Override
    public LogRecord add(LogRecord logRecord) throws BusinessException {
        logRecord = logRecordRepository.save(logRecord);
        return logRecord;
    }

    @Override
    public LogRecord update(LogRecord logRecord) throws BusinessException {
        logRecord = logRecordRepository.save(logRecord);
        return logRecord;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        for (int id : ids) {
            logRecordRepository.delete(id);
        }
    }

    @Override
    public void deleteByTime(Date logRetainTime) throws BusinessException {
        this.logRecordRepository.deleteByTime(logRetainTime);
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
            Page<LogRecord> list = logRecordRepository.findAll(new MySpecification<LogRecord>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<LogRecord> list = logRecordRepository.findAll(new MySpecification<LogRecord>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public LogRecord get(Integer id) throws BusinessException {
        LogRecord logRecord = logRecordRepository.findOne(id);
        if (logRecord == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return logRecord;
    }

}
