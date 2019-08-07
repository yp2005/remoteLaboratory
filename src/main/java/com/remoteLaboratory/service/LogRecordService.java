package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.Date;
import java.util.List;

/**
 * 日志记录服务接口定义
 *
 * @Author: yupeng
 */

public interface LogRecordService {
    /**
     * 添加日志记录信息
     * @param logRecord
     * @return
     */
    public LogRecord add(LogRecord logRecord) throws BusinessException;

    /**
     * 修改日志记录信息
     * @param logRecord
     * @return
     */
    public LogRecord update(LogRecord logRecord) throws BusinessException;

    /**
     * 根据条件查询日志记录信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取日志记录信息
     * @param id
     * @return LogRecord
     */
    public LogRecord get(Integer id) throws BusinessException;

    /**
     * 修改日志记录信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;

    /**
     * 根据时间删除日志
     * @param logRetainTime
     */
    public void deleteByTime(Date logRetainTime) throws BusinessException;
}
