package com.remoteLaboratory.controller;

import com.alibaba.fastjson.JSONObject;
import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.entities.UserOnlineTime;
import com.remoteLaboratory.redis.RedisClient;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.UserRepository;
import com.remoteLaboratory.service.UserOnlineTimeService;
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
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.security.Principal;
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
    private UserOnlineTimeService userOnlineTimeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private LogRecordRepository logRecordRepository;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostConstruct
    private void initUser() throws Exception {
        User user = this.userRepository.findByUserName(Constants.ADMIN_USER_NAME);
        if (user == null) { // 初始化管理员账号
            user = new User();
            user.setPersonName(Constants.ADMIN_USER_NAME);
            user.setUserName(Constants.ADMIN_USER_NAME);
            user.setUserType(Constants.USER_TYPE_ADMIN);
            user.setForumForbidden(0);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(EncodeUtils.encodeSHA("123456".getBytes())));
            userRepository.save(user);
        }
        user = this.userRepository.findByUserName(Constants.GUEST_USER_NAME);
        if (user == null) { // 初始化游客账号
            user = new User();
            user.setPersonName(Constants.GUEST_USER_NAME);
            user.setUserName(Constants.GUEST_USER_NAME);
            user.setUserType(Constants.USER_TYPE_GUEST);
            user.setForumForbidden(0);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(EncodeUtils.encodeSHA("123456".getBytes())));
            userRepository.save(user);
        }
    }

    @PostMapping(path = "/teacherList")
    @ApiOperation(value = "老师列表", notes = "查询老师信息列表")
    public CommonResponse teacherList(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(userService.listByUserType(listInput, Constants.USER_TYPE_TEACHER));
        LogUtil.add(this.logRecordRepository, "查询老师列表", "用户", loginUser, null, null);
        return commonResponse;
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
        user.setPassword(encoder.encode(user.getPassword()));
        user = userService.add(user);
        CommonResponse commonResponse = CommonResponse.getInstance(user);
        LogUtil.add(this.logRecordRepository, "添加", "用户", loginUser, user.getId(), user.getUserName());
        return commonResponse;
    }

    @PostMapping(path = "/forumForbidden/{userId}/{status}")
    @ApiOperation(value = "禁言(0-解禁 1-禁言)", notes = "禁言接口(0-解禁 1-禁言)")
    @LoginRequired(adminRequired = "1")
    public CommonResponse forumForbidden(@PathVariable("userId") Integer userId, @PathVariable("status") Integer status, @ApiIgnore User loginUser) throws BusinessException {
        User user = this.userService.get(userId);
        user.setForumForbidden(status);
        user = this.userService.update(user);
        CommonResponse commonResponse = CommonResponse.getInstance(user);
        LogUtil.add(this.logRecordRepository, status.equals(1) ? "禁言" : "解禁", "用户", loginUser, user.getId(), user.getUserName());
        return commonResponse;
    }

    @PostMapping(path = "/register")
    @ApiOperation(value = "注册", notes = "注册接口")
    public CommonResponse register(@Validated({User.Validation.class}) @RequestBody User user, @ApiIgnore User loginUser) throws BusinessException {
        if(!user.getUserType().equals(Constants.USER_TYPE_STUDENT)) {
            throw new BusinessException(Messages.CODE_40010, "只能注册学生账号！");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user = userService.add(user);
        CommonResponse commonResponse = CommonResponse.getInstance(user);
        LogUtil.add(this.logRecordRepository, "注册", "用户", user, null, null);
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改用户(管理员接口)", notes = "修改用户信息接口(管理员接口)")
    @LoginRequired(adminRequired = "1")
    public CommonResponse update(@Validated({User.Validation.class}) @RequestBody User user, @ApiIgnore User loginUser) throws BusinessException {
        if(StringUtils.isEmpty(user.getPassword())) { // 不修改密码
            User oldUser = this.userRepository.findOne(user.getId());
            user.setPassword(oldUser.getPassword());
        }
        else { // 修改密码
            user.setPassword(encoder.encode(user.getPassword()));
        }
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
        User user = this.userRepository.findOne(newUser.getId());
        newUser.setPassword(user.getPassword());
        newUser = userService.update(newUser);
        CommonResponse commonResponse = CommonResponse.getInstance(loginUser);
        LogUtil.add(this.logRecordRepository, "修改", "用户", loginUser, newUser.getId(), newUser.getUserName());
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

    @GetMapping(path = "/current")
    @ApiOperation(value = "查询当前用户信息", notes = "查询当前用户信息接口")
    @LoginRequired
    public CommonResponse current(@ApiIgnore User loginUser) throws BusinessException {
        User user = userService.get(loginUser.getId());
        user.setToken(loginUser.getToken());
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
            String token = UUID.randomUUID().toString();
            redisClient.set(Constants.USER_TOKEN + token, JSONObject.toJSONString(user), Constants.TOKEN_EXPIRE_TIME);
            redisClient.set(Constants.USER_LOGIN_TIME + token, JSONObject.toJSONString(user));
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
        // 记录在线时间
        UserOnlineTime userOnlineTime = new UserOnlineTime();
        userOnlineTime.setUserId(loginUser.getId());
        userOnlineTime.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
        userOnlineTime.setLoginTime(loginUser.getLastLoginTime());
        userOnlineTime.setLoginOutTime(new Date());
        userOnlineTime.setOnlineTime(userOnlineTime.getLoginOutTime().getTime() - userOnlineTime.getLoginTime().getTime());
        userOnlineTime = this.userOnlineTimeService.add(userOnlineTime);
        this.redisClient.remove(Constants.USER_LOGIN_TIME + loginUser.getToken());
        LogUtil.add(this.logRecordRepository, "登出", "用户", loginUser, null, null);
        return CommonResponse.getInstance();
    }

    @PostMapping(path = "/modifyPassword")
    @ApiOperation(value = "用户修改自己的密码", notes = "用户修改自己的密码接口")
    @LoginRequired
    public CommonResponse modifyPassword(@RequestParam("password") String password, @ApiIgnore User loginUser) throws Exception {
        loginUser = this.userRepository.findOne(loginUser.getId());
        loginUser.setPassword(encoder.encode(password));
        loginUser = this.userService.update(loginUser);
        LogUtil.add(this.logRecordRepository, "修改密码", "用户", loginUser, loginUser.getId(), loginUser.getUserName());
        return CommonResponse.getInstance();
    }

    @PostMapping(path = "/adminModifyPassword")
    @ApiOperation(value = "管理员修改密码", notes = "管理员修改密码接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse adminModifyPassword(@RequestBody User user, @ApiIgnore User loginUser) throws Exception {
        User oldUser = this.userService.get(user.getId());
        oldUser.setPassword(encoder.encode(user.getPassword()));
        oldUser = this.userService.update(oldUser);
        LogUtil.add(this.logRecordRepository, "修改密码", "用户", loginUser, oldUser.getId(), oldUser.getUserName());
        return CommonResponse.getInstance();
    }

    @PostMapping(path = "/importStudent")
    @ApiOperation(value = "excel批量导入学生账号", notes = "excel批量导入学生账号接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse importStudent(@RequestParam MultipartFile file, @ApiIgnore User loginUser) throws BusinessException {
        if (file.isEmpty()) {
            throw new BusinessException(Messages.CODE_40001);
        } else {
            try {
                String originFileName = file.getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                Workbook workbook;
                InputStream is = file.getInputStream();
                if (".xls".equals(ext)) {
                    workbook = new HSSFWorkbook(is);
                } else if (".xlsx".equals(ext)) {
                    workbook = new XSSFWorkbook(is);
                } else {
                    throw new BusinessException(Messages.CODE_40001);
                }
                if(workbook != null) {
                    // 解析上传文件保存车牌数据
                    this.userService.importStudent(workbook);
                }
                else {
                    throw new BusinessException(Messages.CODE_40010, "无法解析的文件");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(Messages.CODE_40010, e.getMessage());
            }
        }
        LogUtil.add(this.logRecordRepository, "导入学生账号", "用户", loginUser, null, null);
        return CommonResponse.getInstance();
    }
}
