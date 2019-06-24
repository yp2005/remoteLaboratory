package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.LogRecordService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 日志记录接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/logRecord")
@LoginRequired(teacherRequired = "1")
@Api(description = "日志记录")
public class LogRecordController {

    private static Logger log = LoggerFactory.getLogger(LogRecordController.class);

    @Autowired
    private LogRecordService logRecordService;

    @Autowired
    private LogRecordRepository logRecordRepository;


    @PostMapping(path = "/list")
    @ApiOperation(value = "日志记录列表", notes = "查询日志记录信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(logRecordService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "日志记录", loginUser, null, null);
        return commonResponse;
    }
}
