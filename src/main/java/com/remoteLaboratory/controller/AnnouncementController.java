package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Announcement;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.AnnouncementService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
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
 * 公告接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/announcement")
@Api(description = "公告")
@LoginRequired
public class AnnouncementController {

    private static Logger log = LoggerFactory.getLogger(AnnouncementController.class);

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "公告列表", notes = "查询公告信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(announcementService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "公告", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加公告", notes = "添加公告信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse add(@Validated({Announcement.Validation.class}) @RequestBody Announcement announcement, @ApiIgnore User loginUser) throws BusinessException {
        announcement = announcementService.add(announcement);
        CommonResponse commonResponse = CommonResponse.getInstance(announcement);
        LogUtil.add(this.logRecordRepository, "添加", "公告", loginUser, announcement.getId(), announcement.getTitle());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改公告(管理员接口)", notes = "修改公告信息接口(管理员接口)")
    @LoginRequired(adminRequired = "1")
    public CommonResponse update(@Validated({Announcement.Validation.class}) @RequestBody Announcement announcement, @ApiIgnore User loginUser) throws BusinessException {
        announcement = announcementService.update(announcement);
        CommonResponse commonResponse = CommonResponse.getInstance(announcement);
        LogUtil.add(this.logRecordRepository, "修改", "公告", loginUser, announcement.getId(), announcement.getTitle());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除公告", notes = "删除公告信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse delete(@NotNull(message = "公告编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        announcementService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询公告", notes = "根据ID查询公告")
    public CommonResponse get(@NotNull(message = "公告编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Announcement announcement = announcementService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(announcement);
        LogUtil.add(this.logRecordRepository, "查询", "公告", loginUser, announcement.getId(), announcement.getTitle());
        return commonResponse;
    }
}
