package com.remoteLaboratory.controller;

import com.alibaba.fastjson.JSONObject;
import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.redis.RedisClient;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.UserRepository;
import com.remoteLaboratory.service.UserService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.EncodeUtils;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/user")
@Api(description = "用户")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostConstruct
    private void initUser() throws Exception {
        User user = this.userRepository.findByUserName(Constants.ADMIN_USER_NAME);
        if (user == null) {
            user = new User();
            user.setPersonName(Constants.ADMIN_USER_NAME);
            user.setUserName(Constants.ADMIN_USER_NAME);
            user.setUserType(Constants.USER_TYPE_ADMIN);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(EncodeUtils.encodeSHA("123456".getBytes())));
            userService.add(user);
        }
    }

    @PostMapping(path = "/list")
    @ApiOperation(value = "用户列表", notes = "查询用户信息列表")
    @LoginRequired(adminRequired = "1")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(userService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "用户", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加用户", notes = "添加用户信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse add(@Validated({User.Validation.class}) @RequestBody User user, @ApiIgnore User loginUser) throws BusinessException {
        user = userService.add(user);
        CommonResponse commonResponse = CommonResponse.getInstance(user);
        LogUtil.add(this.logRecordRepository, "添加", "用户", loginUser, user.getId(), user.getUserName());
        return commonResponse;
    }

    @PostMapping(path = "/register")
    @ApiOperation(value = "注册", notes = "注册接口")
    public CommonResponse register(@Validated({User.Validation.class}) @RequestBody User user, @ApiIgnore User loginUser) throws BusinessException {
        if(!user.getUserType().equals(Constants.USER_TYPE_STUDENT)) {
            throw new BusinessException(Messages.CODE_40010, "只能注册学生账号！");
        }
        user = userService.add(user);
        CommonResponse commonResponse = CommonResponse.getInstance(user);
        LogUtil.add(this.logRecordRepository, "注册", "用户", user, null, null);
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改用户(管理员接口)", notes = "修改用户信息接口(管理员接口)")
    @LoginRequired(adminRequired = "1")
    public CommonResponse update(@Validated({User.Validation.class}) @RequestBody User user, @ApiIgnore User loginUser) throws BusinessException {
        user = userService.update(user);
        CommonResponse commonResponse = CommonResponse.getInstance(user);
        LogUtil.add(this.logRecordRepository, "修改", "用户", loginUser, user.getId(), user.getUserName());
        return commonResponse;
    }

    @PutMapping(path = "modifyUserInfo")
    @ApiOperation(value = "修改用户(用户修改自己的用户信息)", notes = "修改用户信息接口(用户修改自己的用户信息)")
    @LoginRequired
    public CommonResponse modifyUserInfo(@Validated({User.Validation.class}) @RequestBody User newUser, @ApiIgnore User loginUser) throws BusinessException {
        if(!newUser.getId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(!newUser.getUserName().equals(loginUser.getUserName())) {
            throw new BusinessException(Messages.CODE_40010, "用户名不允许修改！");
        }
        loginUser = userService.update(loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance(loginUser);
        LogUtil.add(this.logRecordRepository, "修改", "用户", loginUser, loginUser.getId(), loginUser.getUserName());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户", notes = "删除用户信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse delete(@NotNull(message = "用户编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        userService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询用户", notes = "根据ID查询用户")
    @LoginRequired
    public CommonResponse get(@NotNull(message = "用户编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        User user = userService.get(id);
        // 管理账户可以查看所有用户信息，学生只能查看自己的信息
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !loginUser.getId().equals(user.getId())) {
            throw new BusinessException(Messages.CODE_50201);
        }
        CommonResponse commonResponse = CommonResponse.getInstance(user);
        LogUtil.add(this.logRecordRepository, "查询", "用户", loginUser, user.getId(), user.getUserName());
        return commonResponse;
    }

    @PostMapping(path = "/login")
    public CommonResponse login(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
        User user = this.userRepository.findByUserName(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(password, user.getPassword())) {
            user.setLastLoginTime(new Date());
            user = this.userRepository.save(user);
            // TODO 记录在线时间
            String token = UUID.randomUUID().toString();
            redisClient.set(Constants.USER_TOKEN + token, JSONObject.toJSONString(user), Constants.TOKEN_EXPIRE_TIME);
            user.setToken(token);
            CommonResponse commonResponse = CommonResponse.getInstance(user);
            LogUtil.add(this.logRecordRepository, "登陆", "用户", user, null, null);
            return commonResponse;
        }
        else {
            throw new BusinessException(Messages.CODE_40101);
        }
    }

    @PostMapping(path = "/loginout")
    @LoginRequired
    public CommonResponse loginout( @ApiIgnore User loginUser) throws Exception {
        redisClient.remove(Constants.USER_TOKEN + loginUser.getToken());
        // TODO 记录在线时间
        LogUtil.add(this.logRecordRepository, "登出", "用户", loginUser, null, null);
        return CommonResponse.getInstance();
    }

    @PostMapping(path = "/modifyPassword")
    @LoginRequired
    public CommonResponse modifyPassword(@RequestParam("password") String password, @ApiIgnore User loginUser) throws Exception {
        loginUser = this.userRepository.findByUserName(loginUser.getUserName());
        loginUser.setPassword(password);
        this.userService.update(loginUser);
        LogUtil.add(this.logRecordRepository, "修改密码", "用户", loginUser, null, null);
        return CommonResponse.getInstance();
    }
}
