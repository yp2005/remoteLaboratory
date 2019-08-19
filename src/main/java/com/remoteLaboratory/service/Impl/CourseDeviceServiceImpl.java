package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.CourseDeviceRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseDeviceService;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.DeviceService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.CourseDeviceListAddInput;
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
 * 课程设备服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class CourseDeviceServiceImpl implements CourseDeviceService {
    private static Logger log = LoggerFactory.getLogger(CourseDeviceServiceImpl.class);

    private CourseDeviceRepository courseDeviceRepository;

    private LogRecordRepository logRecordRepository;

    private CourseService courseService;

    private DeviceService deviceService;

    @Autowired
    public CourseDeviceServiceImpl(CourseDeviceRepository courseDeviceRepository,
                                   CourseService courseService,
                                   DeviceService deviceService,
                                   LogRecordRepository logRecordRepository) {
        this.courseDeviceRepository = courseDeviceRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
        this.deviceService = deviceService;
    }

    @Override
    public CourseDevice add(CourseDevice courseDevice) throws BusinessException {
        CourseDevice cd = this.courseDeviceRepository.findByCourseIdAndDeviceId(courseDevice.getCourseId(), courseDevice.getDeviceId());
        if(cd != null) {
            throw new BusinessException(Messages.CODE_40010, "该课程已存在该设备");
        }
        courseDevice = courseDeviceRepository.save(courseDevice);
        return courseDevice;
    }

    @Override
    public List<CourseDevice> listAdd(CourseDeviceListAddInput courseDeviceListAddInput) throws BusinessException {
        List<CourseDevice> courseDeviceList = new ArrayList<>();
        for(int deviceId : courseDeviceListAddInput.getDeviceIds()) {
            CourseDevice cd = this.courseDeviceRepository.findByCourseIdAndDeviceId(courseDeviceListAddInput.getCourseId(), deviceId);
            if(cd != null) {
                throw new BusinessException(Messages.CODE_40010, "该课程已存在该设备");
            }
            Device device = this.deviceService.get(deviceId);
            CourseDevice courseDevice = new CourseDevice();
            courseDevice.setCourseId(courseDeviceListAddInput.getCourseId());
            courseDevice.setCourseName(courseDeviceListAddInput.getCourseName());
            courseDevice.setDeviceId(deviceId);
            courseDevice.setDeviceName(device.getName());
            courseDevice.setResourceClass(device.getResourceClass());
            courseDevice.setPicture(device.getPicture());
            courseDevice.setType(device.getType());
            courseDevice = this.courseDeviceRepository.save(courseDevice);
            courseDeviceList.add(courseDevice);
        }
        return courseDeviceList;
    }

    @Override
    public CourseDevice update(CourseDevice courseDevice) throws BusinessException {
        courseDevice = courseDeviceRepository.save(courseDevice);
        return courseDevice;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            CourseDevice courseDevice = this.courseDeviceRepository.findOne(id);
            if(courseDevice != null) {
                Course course = this.courseService.get(courseDevice.getCourseId());
                if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                if(course.getStatus().equals(1)) {
                    throw new BusinessException(Messages.CODE_40010, "课程正在进行不能删除！");
                }
                courseDeviceRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("课程设备");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(courseDevice.getId());
                logRecord.setObjectName(courseDevice.getCourseName() + "->" + courseDevice.getDeviceName());
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
            Page<CourseDevice> list = courseDeviceRepository.findAll(new MySpecification<CourseDevice>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<CourseDevice> list = courseDeviceRepository.findAll(new MySpecification<CourseDevice>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public CourseDevice get(Integer id) throws BusinessException {
        CourseDevice courseDevice = courseDeviceRepository.findOne(id);
        if (courseDevice == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return courseDevice;
    }

}
