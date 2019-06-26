package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.Exercise;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.ExerciseService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.ExerciseUtil;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
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
 * 习题接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/exercise")
@Api(description = "习题")
@LoginRequired(teacherRequired = "1")
public class ExerciseController {

    private static Logger log = LoggerFactory.getLogger(ExerciseController.class);

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private CourseService courseService;

    @PostMapping(path = "/list")
    @ApiOperation(value = "习题列表", notes = "查询习题信息列表")
    public CommonResponse list(@RequestBody ListInput listInput, @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(exerciseService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "习题", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加习题", notes = "添加习题信息接口")
    public CommonResponse add(@Validated({Exercise.Validation.class}) @RequestBody Exercise exercise, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(exercise.getCourseId());
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        exercise.setType(ExerciseUtil.getType(exercise.getExercisesType()));
        exercise = exerciseService.add(exercise);
        CommonResponse commonResponse = CommonResponse.getInstance(exercise);
        LogUtil.add(this.logRecordRepository, "添加", "习题", loginUser, exercise.getId(), ExerciseUtil.getTypeName(exercise.getExercisesType()));
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改习题", notes = "修改习题信息接口")
    public CommonResponse update(@Validated({Exercise.Validation.class}) @RequestBody Exercise exercise, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(exercise.getCourseId());
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        exercise.setType(ExerciseUtil.getType(exercise.getExercisesType()));
        exercise = exerciseService.update(exercise);
        CommonResponse commonResponse = CommonResponse.getInstance(exercise);
        LogUtil.add(this.logRecordRepository, "修改", "习题", loginUser, exercise.getId(), ExerciseUtil.getTypeName(exercise.getExercisesType()));
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除习题", notes = "删除习题信息接口")
    public CommonResponse delete(@NotNull(message = "习题编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        exerciseService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询习题", notes = "根据ID查询习题")
    public CommonResponse get(@NotNull(message = "习题编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Exercise exercise = exerciseService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(exercise);
        LogUtil.add(this.logRecordRepository, "查询", "习题", loginUser, exercise.getId(), ExerciseUtil.getTypeName(exercise.getExercisesType()));
        return commonResponse;
    }
}
