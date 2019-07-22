package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.UploadFile;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.UploadFileService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传restful接口类
 *
 * @Author: yupeng
 */

@RestController
@RequestMapping("/uploadFile")
@Api(description = "上传文件")
@LoginRequired
public class UploadFileController {

    private static Logger log = LoggerFactory.getLogger(UploadFileController.class);

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Value("${UPLOAD_FILE_PATH}")
    private String uploadFilePath;

    @PostMapping(path = "/upload")
    @ApiOperation(value = "文件上传", notes = "文件上传")
    public CommonResponse upload(@NotNull(message = "上传文件不能为空") @RequestParam("file") MultipartFile mfile, @RequestParam("name") String originFileName, @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        Calendar c = Calendar.getInstance();
        // 每个月生成一个文件夹来放文件
        String timePath = "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH);
        String path = this.uploadFilePath + File.separator + loginUser.getUserName() + File.separator + timePath;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        log.info("文件上传：" + originFileName);
        if (mfile.isEmpty() ) {
            commonResponse.setCode(Messages.CODE_40001);
            commonResponse.setMessage("上传文件为空");
        } else {
            String suffix = originFileName.split("\\.")[originFileName.split("\\.").length - 1];
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
            File file = new File(path, fileName);
            try {
                FileUtils.copyInputStreamToFile(mfile.getInputStream(), file);
                String uploadFileName = originFileName + "|" + loginUser.getUserName() + "/" + timePath + "/" + fileName;
                commonResponse.setResult(uploadFileName);
                UploadFile uploadFile = new UploadFile();
                uploadFile.setName(originFileName);
                uploadFile.setUploadFileName(uploadFileName);
                uploadFile.setUserId(loginUser.getId());
                uploadFile.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                uploadFile = this.uploadFileService.add(uploadFile);
                commonResponse.setResult(uploadFile);
                LogUtil.add(this.logRecordRepository, "上传文件", "上传文件", loginUser, uploadFile.getId(), uploadFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
                commonResponse.setCode(Messages.CODE_50000);
                commonResponse.setMessage(e.getMessage());
            }
        }
        return commonResponse;
    }

    @PostMapping(path = "/myList")
    @ApiOperation(value = "我的上传文件列表", notes = "查询我的上传文件列表")
    public CommonResponse myList(@RequestBody ListInput listInput, @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(uploadFileService.listByUserId(listInput, loginUser.getId()));
        LogUtil.add(this.logRecordRepository, "列表查询", "上传文件", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping(path = "/list")
    @ApiOperation(value = "上传文件列表", notes = "查询上传文件信息列表")
    @LoginRequired(adminRequired = "1")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(uploadFileService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "上传文件", loginUser, null, null);
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除上传文件(管理员操作接口)", notes = "删除上传文件信息接口(管理员操作接口)")
    @LoginRequired(adminRequired = "1")
    public CommonResponse delete(@NotNull(message = "上传文件编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        uploadFileService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @DeleteMapping(path = "/deleteById/{id}")
    @ApiOperation(value = "根据ID删除上传文件", notes = "根据ID删除上传文件信息接口")
    public CommonResponse deleteById(@NotNull(message = "上传文件编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        UploadFile uploadFile = this.uploadFileService.get(id);
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !uploadFile.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        uploadFileService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }
}
