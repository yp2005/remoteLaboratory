package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.TestExerciseInstanceService;
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
 * 小题实例接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/testExerciseInstance")
@Api(description = "小题实例")
@LoginRequired
public class TestExerciseInstanceController {

    private static Logger log = LoggerFactory.getLogger(TestExerciseInstanceController.class);

    @Autowired
    private TestExerciseInstanceService testExerciseInstanceService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "小题实例列表", notes = "查询小题实例信息列表")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testExerciseInstanceService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "小题实例", loginUser, null, null);
        return commonResponse;
    }
}
