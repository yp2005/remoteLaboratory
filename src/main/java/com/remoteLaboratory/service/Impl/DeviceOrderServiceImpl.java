package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.DeviceOrder;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.DeviceOrderRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.DeviceOrderService;
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
import java.util.Date;
import java.util.List;

/**
 * 设备预约服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class DeviceOrderServiceImpl implements DeviceOrderService {
    private static Logger log = LoggerFactory.getLogger(DeviceOrderServiceImpl.class);

    private DeviceOrderRepository deviceOrderRepository;

    private LogRecordRepository logRecordRepository;

    private CourseService courseService;

    @Autowired
    public DeviceOrderServiceImpl(DeviceOrderRepository deviceOrderRepository, LogRecordRepository logRecordRepository, CourseService courseService) {
        this.deviceOrderRepository = deviceOrderRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
    }

    @Override
    public DeviceOrder add(DeviceOrder deviceOrder) throws BusinessException {
        deviceOrder = deviceOrderRepository.save(deviceOrder);
        return deviceOrder;
    }

    @Override
    public DeviceOrder update(DeviceOrder deviceOrder) throws BusinessException {
        deviceOrder = deviceOrderRepository.save(deviceOrder);
        return deviceOrder;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            DeviceOrder deviceOrder = this.deviceOrderRepository.findOne(id);
            if(deviceOrder != null) {
                if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                deviceOrderRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("设备预约");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(deviceOrder.getId());
                logRecord.setObjectName(deviceOrder.getUserName() + ": " + deviceOrder.getDeviceName());
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
            Page<DeviceOrder> list = deviceOrderRepository.findAll(new MySpecification<DeviceOrder>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<DeviceOrder> list = deviceOrderRepository.findAll(new MySpecification<DeviceOrder>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public DeviceOrder get(Integer id) throws BusinessException {
        DeviceOrder deviceOrder = deviceOrderRepository.findOne(id);
        if (deviceOrder == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return deviceOrder;
    }

    @Override
    public DeviceOrder getByUserId(Integer userId) throws BusinessException {
        DeviceOrder deviceOrder = deviceOrderRepository.findByUserIdAndTime(userId, new Date());
        if (deviceOrder == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return deviceOrder;
    }

    @Override
    public DeviceOrder order(Integer id, User user) throws BusinessException {
        DeviceOrder deviceOrder = deviceOrderRepository.findByUserIdAndTime(user.getId(), new Date());
        if (deviceOrder != null) {
            throw new BusinessException(Messages.CODE_40010, "您已有预约设备，请完成实验后再进行预约！");
        }
        deviceOrder = this.deviceOrderRepository.findOne(id);
        if (deviceOrder == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        deviceOrder.setUserId(user.getId());
        deviceOrder.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
        deviceOrder.setStatus(1);
        return deviceOrder;
    }

}