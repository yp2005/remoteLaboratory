package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Reply;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.UserRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.ReplyService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 回复接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/reply")
@Api(description = "回复")
@LoginRequired
public class ReplyController {

    private static Logger log = LoggerFactory.getLogger(ReplyController.class);

    @Autowired
    private ReplyService replyService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "回复列表", notes = "查询回复信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(replyService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "回复", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加回复", notes = "添加回复信息接口")
    public CommonResponse add(@Validated({Reply.Validation.class}) @RequestBody Reply reply, @ApiIgnore User loginUser) throws BusinessException {
        loginUser = this.userRepository.findOne(loginUser.getId());
        if(loginUser.getForumForbidden().equals(1)) {
            throw new BusinessException(Messages.CODE_40010, "你被禁言了！");
        }
        reply.setUserId(loginUser.getId());
        reply.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
        reply.setUserImage(loginUser.getUserImage());
        reply.setUserType(loginUser.getUserType());
        reply.setReplyNumber(0);
        reply = replyService.add(reply);
        CommonResponse commonResponse = CommonResponse.getInstance(reply);
        LogUtil.add(this.logRecordRepository, "添加", "回复", loginUser, reply.getId(), reply.getSubjectTitle());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除回复", notes = "删除回复信息接口")
    public CommonResponse delete(@NotNull(message = "回复编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        replyService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询回复", notes = "根据ID查询回复")
    public CommonResponse get(@NotNull(message = "回复编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Reply reply = replyService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(reply);
        LogUtil.add(this.logRecordRepository, "查询", "回复", loginUser, reply.getId(), reply.getSubjectTitle());
        return commonResponse;
    }
}
