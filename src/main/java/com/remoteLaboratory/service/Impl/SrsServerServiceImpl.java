package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.Camera;
import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.SrsServer;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.CameraRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.SrsServerRepository;
import com.remoteLaboratory.service.SrsServerService;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.SrsAgentUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import com.remoteLaboratory.vo.SrsServerAgentVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * srs服务服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class SrsServerServiceImpl implements SrsServerService {
    private static Logger log = LoggerFactory.getLogger(SrsServerServiceImpl.class);

    private SrsServerRepository srsServerRepository;

    private CameraRepository cameraRepository;

    private LogRecordRepository logRecordRepository;


    @Autowired
    public SrsServerServiceImpl(SrsServerRepository srsServerRepository,
                                CameraRepository cameraRepository,
                                LogRecordRepository logRecordRepository) {
        this.srsServerRepository = srsServerRepository;
        this.cameraRepository = cameraRepository;
        this.logRecordRepository = logRecordRepository;
    }

    @Override
    public SrsServer add(SrsServer srsServer) throws BusinessException {
        srsServer = srsServerRepository.save(srsServer);
        // 调用SRS代理客服端更新配置
        SrsAgentUtil srsAgentUtil = new SrsAgentUtil();
        srsAgentUtil.refreshConfig(srsServer.getUrl());
        return srsServer;
    }

    @Override
    public SrsServer update(SrsServer srsServer) throws BusinessException {
        srsServer = srsServerRepository.save(srsServer);
        // 调用SRS代理客服端更新配置
        SrsAgentUtil srsAgentUtil = new SrsAgentUtil();
        srsAgentUtil.refreshConfig(srsServer.getUrl());
        return srsServer;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<SrsServer> srsServers = new ArrayList<>();
        List<LogRecord> logRecords = new ArrayList<>();
        for (Integer id : ids) {
            List<Camera> cameras = this.cameraRepository.findBySrsServerId(id);
            if(CollectionUtils.isNotEmpty(cameras)) {
                throw new BusinessException(Messages.CODE_40010, "srs服务下已关联摄像头");
            }
            SrsServer srsServer = this.srsServerRepository.findOne(id);
            if(srsServer != null) {
                srsServers.add(srsServer);
                this.srsServerRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("srs服务");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(srsServer.getId());
                logRecord.setObjectName(srsServer.getName());
                logRecords.add(logRecord);
            }
        }
        // 调用SRS代理客服端更新配置
        srsServers.forEach(srsServer -> {
            SrsAgentUtil srsAgentUtil = new SrsAgentUtil();
            srsAgentUtil.refreshConfig(srsServer.getUrl());
        });
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
            Page<SrsServer> list = srsServerRepository.findAll(new MySpecification<SrsServer>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<SrsServer> list = srsServerRepository.findAll(new MySpecification<SrsServer>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public SrsServer get(Integer id) throws BusinessException {
        SrsServer srsServer = srsServerRepository.findOne(id);
        if (srsServer == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return srsServer;
    }

    @Override
    public SrsServerAgentVo getByUniqueKey(String uniqueKey) throws BusinessException {
        SrsServer srsServer = srsServerRepository.findByUniqueKey(uniqueKey);
        if (srsServer == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        SrsServerAgentVo srsServerAgentVo = new SrsServerAgentVo(srsServer);
        List<Camera> cameras = this.cameraRepository.findBySrsServerId(srsServer.getId());
        srsServerAgentVo.setCameraList(cameras);
        return srsServerAgentVo;
    }
}
