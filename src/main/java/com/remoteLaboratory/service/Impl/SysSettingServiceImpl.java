package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.SysSetting;
import com.remoteLaboratory.redis.RedisClient;
import com.remoteLaboratory.repositories.SysSettingRepository;
import com.remoteLaboratory.service.SysSettingService;
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
import java.util.List;

/**
 * 系统设置服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class SysSettingServiceImpl implements SysSettingService {
    private static Logger log = LoggerFactory.getLogger(SysSettingServiceImpl.class);

    private SysSettingRepository sysSettingRepository;

    private RedisClient redisClient;

    @Autowired
    public SysSettingServiceImpl(SysSettingRepository sysSettingRepository, RedisClient redisClient) {
        this.sysSettingRepository = sysSettingRepository;
        this.redisClient = redisClient;
    }

    @Override
    public SysSetting add(SysSetting sysSetting) throws BusinessException {
        return sysSettingRepository.save(sysSetting);
    }

    @Override
    public SysSetting update(SysSetting sysSetting) throws BusinessException {
        sysSetting = sysSettingRepository.save(sysSetting);
        try {
            this.redisClient.set(sysSetting.getKeyName(), sysSetting.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysSetting;
    }

    @Override
    public void delete(List<String> ids) throws BusinessException {
        for (String id : ids) {
            sysSettingRepository.delete(id);
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
            Page<SysSetting> list = sysSettingRepository.findAll(new MySpecification<SysSetting>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<SysSetting> list = sysSettingRepository.findAll(new MySpecification<SysSetting>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public SysSetting get(String id) throws BusinessException {
        SysSetting sysSetting = sysSettingRepository.findOne(id);
        if (sysSetting == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return sysSetting;
    }

    @Override
    public SysSetting getByKeyName(String keyName) throws BusinessException {
        SysSetting sysSetting = sysSettingRepository.findByKeyName(keyName);
        if (sysSetting == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return sysSetting;
    }
}
