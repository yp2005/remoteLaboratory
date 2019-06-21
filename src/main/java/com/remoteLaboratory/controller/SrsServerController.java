package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.SrsServer;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.SrsServerService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.SrsServerAgentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * srs服务接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/srsServer")
@Api(description = "srs服务")
@LoginRequired(adminRequired = "1")
public class SrsServerController {

    private static Logger log = LoggerFactory.getLogger(SrsServerController.class);

    @Autowired
    private SrsServerService srsServerService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "srs服务列表", notes = "查询srs服务信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(srsServerService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "srs服务", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加srs服务", notes = "添加srs服务信息接口")
    public CommonResponse add(@Validated({SrsServer.Validation.class}) @RequestBody SrsServer srsServer, @ApiIgnore User loginUser) throws BusinessException {
        srsServer = srsServerService.add(srsServer);
        CommonResponse commonResponse = CommonResponse.getInstance(srsServer);
        LogUtil.add(this.logRecordRepository, "添加", "srs服务", loginUser, srsServer.getId(), srsServer.getName());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改srs服务", notes = "修改srs服务信息接口")
    public CommonResponse update(@Validated({SrsServer.Validation.class}) @RequestBody SrsServer srsServer, @ApiIgnore User loginUser) throws BusinessException {
        srsServer = srsServerService.update(srsServer);
        CommonResponse commonResponse = CommonResponse.getInstance(srsServer);
        LogUtil.add(this.logRecordRepository, "修改", "srs服务", loginUser, srsServer.getId(), srsServer.getName());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除srs服务", notes = "删除srs服务信息接口")
    public CommonResponse delete(@NotNull(message = "srs服务编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        srsServerService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询srs服务", notes = "根据ID查询srs服务")
    public CommonResponse get(@NotNull(message = "srs服务编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        SrsServer srsServer = srsServerService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(srsServer);
        LogUtil.add(this.logRecordRepository, "查询", "srs服务", loginUser, srsServer.getId(), srsServer.getName());
        return commonResponse;
    }

    @GetMapping(path = "/getByUniqueKey/{uniqueKey}")
    @ApiOperation(value = "根据uniqueKey查询srs服务", notes = "根据uniqueKey查询srs服务")
    public CommonResponse getByUniqueKey(@NotNull(message = "uniqueKey不能为空") @PathVariable String uniqueKey, @ApiIgnore User loginUser) throws BusinessException {
        SrsServerAgentVo srsServerAgentVo = srsServerService.getByUniqueKey(uniqueKey);
        CommonResponse commonResponse = CommonResponse.getInstance(srsServerAgentVo);
        LogUtil.add(this.logRecordRepository, "查询", "srs服务", loginUser, srsServerAgentVo.getId(), srsServerAgentVo.getName());
        return commonResponse;
    }
}
