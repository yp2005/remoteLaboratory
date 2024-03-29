package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.*;
import com.remoteLaboratory.service.DeviceService;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.BindCameraInput;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 设备服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {
    private static Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private DeviceRepository deviceRepository;

    private LogRecordRepository logRecordRepository;

    private CameraRepository cameraRepository;

    private SignalChannelRepository signalChannelRepository;

    private CourseDeviceRepository courseDeviceRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository,
                             CameraRepository cameraRepository,
                             CourseDeviceRepository courseDeviceRepository,
                             SignalChannelRepository signalChannelRepository,
                             LogRecordRepository logRecordRepository) {
        this.deviceRepository = deviceRepository;
        this.logRecordRepository = logRecordRepository;
        this.cameraRepository = cameraRepository;
        this.signalChannelRepository = signalChannelRepository;
        this.courseDeviceRepository = courseDeviceRepository;
    }

    @Override
    public Device add(Device device) throws BusinessException {
        device = deviceRepository.save(device);
        return device;
    }

    @Override
    public Device update(Device device) throws BusinessException {
        device = deviceRepository.save(device);
        return device;
    }

    @Override
    public Device bindCamera(BindCameraInput bindCameraInput) throws BusinessException {
        Camera camera = this.cameraRepository.findOne(bindCameraInput.getCameraId());
        if(bindCameraInput.getStatus().equals(1) && camera.getBindStatus().equals(1)) {
            throw new BusinessException(Messages.CODE_40010, "摄像头已被绑定");
        }
        Device device = this.deviceRepository.findOne(bindCameraInput.getDeviceId());
        if(bindCameraInput.getStatus().equals(1)) {
            if(device.getCameraId() != null) {
                throw new BusinessException(Messages.CODE_40010, "设备已绑定摄像头");
            }
            device.setCameraId(camera.getId());
            device.setCameraName(camera.getName());
        }
        else {
            device.setCameraId(null);
            device.setCameraName(null);
        }
        device = this.deviceRepository.save(device);
        camera.setBindStatus(bindCameraInput.getStatus());
        camera = this.cameraRepository.save(camera);
        return device;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            Device device = deviceRepository.findOne(id);
            if (device != null) {
                List<CourseDevice> courseDeviceList = this.courseDeviceRepository.findByDeviceId(id);
                if(CollectionUtils.isNotEmpty(courseDeviceList)) {
                    throw new BusinessException(Messages.CODE_40010, "设备已有课程使用，不能删除");
                }
                deviceRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("设备");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(device.getId());
                logRecord.setObjectName(device.getName());
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
            Page<Device> list = deviceRepository.findAll(new MySpecification<Device>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<Device> list = deviceRepository.findAll(new MySpecification<Device>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public Device get(Integer id) throws BusinessException {
        Device device = deviceRepository.findOne(id);
        if (device == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        if(device.getType().equals(2)) { // 信号检测设备查询信道
            device.setSignalChannelList(this.signalChannelRepository.findByDeviceId(id));
        }
        return device;
    }
}
