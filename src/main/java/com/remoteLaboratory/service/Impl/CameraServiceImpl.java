package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.Camera;
import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.SrsServer;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.CameraRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.SrsServerRepository;
import com.remoteLaboratory.service.CameraService;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.SrsAgentUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.CameraPublicVo;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
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

import javax.transaction.Transactional;
import java.util.*;

/**
 * 摄像头服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class CameraServiceImpl implements CameraService {
    private static Logger log = LoggerFactory.getLogger(CameraServiceImpl.class);

    private CameraRepository cameraRepository;

    private SrsServerRepository srsServerRepository;

    private LogRecordRepository logRecordRepository;

    @Autowired
    public CameraServiceImpl(CameraRepository cameraRepository,
                             LogRecordRepository logRecordRepository,
                             SrsServerRepository srsServerRepository) {
        this.cameraRepository = cameraRepository;
        this.srsServerRepository = srsServerRepository;
        this.logRecordRepository = logRecordRepository;
    }

    @Override
    public Camera add(Camera camera) throws BusinessException {
        if(StringUtils.isEmpty(camera.getUniqueKey())) {
            camera.setUniqueKey(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        camera = cameraRepository.save(camera);
        // 调用SRS代理客服端更新配置
        if (camera.getSrsServerId() != null) {
            SrsServer srsServer = this.srsServerRepository.findOne(camera.getSrsServerId());
            if (srsServer != null) {
                SrsAgentUtil srsAgentUtil = new SrsAgentUtil();
                srsAgentUtil.refreshConfig(srsServer.getUrl());
            }
        }
        return camera;
    }

    @Override
    public Camera update(Camera camera) throws BusinessException {
        if(StringUtils.isEmpty(camera.getUniqueKey())) {
            camera.setUniqueKey(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        Camera cameraOld = this.cameraRepository.findOne(camera.getId());
        camera = cameraRepository.save(camera);
        // 调用SRS代理客服端更新配置
        // 如果srsServer变更要通知旧的srsServer刷新配置
        if (cameraOld.getSrsServerId() != camera.getSrsServerId()) {
            SrsServer srsServer = this.srsServerRepository.findOne(cameraOld.getSrsServerId());
            if (srsServer != null) {
                SrsAgentUtil srsAgentUtil = new SrsAgentUtil();
                srsAgentUtil.refreshConfig(srsServer.getUrl());
            }
        }

        SrsServer srsServer = this.srsServerRepository.findOne(camera.getSrsServerId());
        if (srsServer != null) {
            SrsAgentUtil srsAgentUtil = new SrsAgentUtil();
            srsAgentUtil.refreshConfig(srsServer.getUrl());
        }
        return camera;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        Set<Integer> srsIds = new HashSet<>(); //srsServer ids
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            Camera camera = cameraRepository.findOne(id);
            if (camera != null) {
                srsIds.add(camera.getSrsServerId());
                cameraRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("摄像头");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(camera.getId());
                logRecord.setObjectName(camera.getName());
                logRecords.add(logRecord);
            }
        }
        // 调用SRS/视频分析代理客服端更新配置
        SrsAgentUtil srsAgentUtil = new SrsAgentUtil();
        for(Integer id : srsIds) {
            SrsServer srsServer = this.srsServerRepository.findOne(id);
            if (srsServer != null) {
                srsAgentUtil.refreshConfig(srsServer.getUrl());
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
            Page<Camera> list = cameraRepository.findAll(new MySpecification<Camera>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            List<CameraPublicVo> cameraPublicVos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(list.getContent())) {
                for (Camera camera : list.getContent()) {
                    CameraPublicVo cameraPublicVo = new CameraPublicVo(camera);
                    if(camera.getSrsServerId() != null) {
                        SrsServer srsServer = this.srsServerRepository.findOne(camera.getSrsServerId());
                        cameraPublicVo.setSrsUrl(srsServer.getUrl());
                        cameraPublicVo.setSrsServerName(srsServer.getName());
                        cameraPublicVo.setSrsUniqueKey(srsServer.getUniqueKey());
                    }
                    cameraPublicVos.add(cameraPublicVo);
                }
            }
            listOutput.setList(cameraPublicVos);
        } else {
            List<Camera> list = cameraRepository.findAll(new MySpecification<Camera>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            List<CameraPublicVo> cameraPublicVos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(list)) {
                for (Camera camera : list) {
                    CameraPublicVo cameraPublicVo = new CameraPublicVo(camera);
                    if(camera.getSrsServerId() != null) {
                        SrsServer srsServer = this.srsServerRepository.findOne(camera.getSrsServerId());
                        cameraPublicVo.setSrsUrl(srsServer.getUrl());
                        cameraPublicVo.setSrsServerName(srsServer.getName());
                        cameraPublicVo.setSrsUniqueKey(srsServer.getUniqueKey());
                    }
                    cameraPublicVos.add(cameraPublicVo);
                }
            }
            listOutput.setList(cameraPublicVos);
        }
        return listOutput;
    }

    @Override
    public CameraPublicVo get(Integer id) throws BusinessException {
        Camera camera = cameraRepository.findOne(id);
        if (camera == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        CameraPublicVo cameraPublicVo = new CameraPublicVo(camera);
        if(camera.getSrsServerId() != null) {
            SrsServer srsServer = this.srsServerRepository.findOne(camera.getSrsServerId());
            cameraPublicVo.setSrsUrl(srsServer.getUrl());
            cameraPublicVo.setSrsServerName(srsServer.getName());
            cameraPublicVo.setSrsUniqueKey(srsServer.getUniqueKey());
        }
        return cameraPublicVo;
    }
}
