package com.remoteLaboratory.vo;

import com.remoteLaboratory.entities.TestExerciseInstance;
import io.swagger.annotations.ApiModel;

/**
 * 测验小题实例未交卷时的输出
 *
 * @Author: yupeng
 */
@ApiModel(value = "测验小题实例未交卷时的输出")
public class TestExerciseInstanceOutput extends TestExerciseInstance{

    public TestExerciseInstanceOutput(TestExerciseInstance testExerciseInstance) {
        this.setId(testExerciseInstance.getTestInstanceId());
        this.setUpdateTime(testExerciseInstance.getUpdateTime());
        this.setCreateTime(testExerciseInstance.getCreateTime());
        this.setAnswer(testExerciseInstance.getAnswer());
        this.setExerciseContent(testExerciseInstance.getExerciseContent());
        this.setExerciseId(testExerciseInstance.getExerciseId());
        this.setExercisesType(testExerciseInstance.getExercisesType());
        this.setOptions(testExerciseInstance.getOptions());
        this.setScore(testExerciseInstance.getScore());
        this.setSerialNumber(testExerciseInstance.getSerialNumber());
        this.setStatus(testExerciseInstance.getStatus());
        this.setTestInstanceId(testExerciseInstance.getTestInstanceId());
        this.setTestPartInstanceId(testExerciseInstance.getTestPartInstanceId());
        this.setType(testExerciseInstance.getExercisesType());
    }
}
