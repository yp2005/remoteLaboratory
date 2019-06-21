package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.Announcement;
import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.AnnouncementRepository;
import com.remoteLaboratory.service.AnnouncementService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 公告服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {
    private static Logger log = LoggerFactory.getLogger(AnnouncementServiceImpl.class);

    private AnnouncementRepository announcementRepository;

    private LogRecordRepository logRecordRepository;

    @Autowired
    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository, LogRecordRepository logRecordRepository) {
        this.announcementRepository = announcementRepository;
        this.logRecordRepository = logRecordRepository;
    }

    @Override
    public Announcement add(Announcement announcement) throws BusinessException {
        announcement = announcementRepository.save(announcement);
        return announcement;
    }

    @Override
    public Announcement update(Announcement announcement) throws BusinessException {
        announcement = announcementRepository.save(announcement);
        return announcement;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            Announcement announcement = this.announcementRepository.findOne(id);
            if(announcement != null) {
                announcementRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("公告");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(announcement.getId());
                logRecord.setObjectName(announcement.getTitle());
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
            Page<Announcement> list = announcementRepository.findAll(new MySpecification<Announcement>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<Announcement> list = announcementRepository.findAll(new MySpecification<Announcement>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public Announcement get(Integer id) throws BusinessException {
        Announcement announcement = announcementRepository.findOne(id);
        if (announcement == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return announcement;
    }

}
