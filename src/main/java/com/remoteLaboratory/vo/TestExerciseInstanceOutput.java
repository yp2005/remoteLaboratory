package com.remoteLaboratory.vo;

import com.remoteLaboratory.entities.TestExerciseInstance;
import io.swagger.annotations.ApiModel;

/**
 * 实验报告小题实例未交卷时的输出
 *
 * @Author: yupeng
 */
@ApiModel(value = "实验报告小题实例未交卷时的输出")
public class TestExerciseInstanceOutput extends TestExerciseInstance {

    public TestExerciseInstanceOutput(TestExerciseInstance testExerciseInstance) {
        this.setId(testExerciseInstance.getId());
        this.setUpdateTime(testExerciseInstance.getUpdateTime());
        this.setCreateTime(testExerciseInstance.getCreateTime());
        this.setAnswer(testExerciseInstance.getAnswer());
        this.setExerciseContent(testExerciseInstance.getExerciseContent());
        this.setExerciseId(testExerciseInstance.getExerciseId());
        this.setTestExerciseTemplateId(testExerciseInstance.getTestExerciseTemplateId());
        this.setExercisesType(testExerciseInstance.getExercisesType());
        this.setOptions(testExerciseInstance.getOptions());
        this.setScore(testExerciseInstance.getScore());
        this.setSerialNumber(testExerciseInstance.getSerialNumber());
        this.setStatus(testExerciseInstance.getStatus());
        this.setTestInstanceId(testExerciseInstance.getTestInstanceId());
        this.setTestPartInstanceId(testExerciseInstance.getTestPartInstanceId());
        this.setTestSubsectionInstanceId(testExerciseInstance.getTestSubsectionInstanceId());
        this.setType(testExerciseInstance.getExercisesType());
        this.setDisplayType(testExerciseInstance.getDisplayType());
    }
}
