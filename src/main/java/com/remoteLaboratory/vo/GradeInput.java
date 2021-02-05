package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 打分入参
 *
 * @Author: yupeng
 */
@ApiModel(value = "打分入参")
public class GradeInput {
    @ApiModelProperty(value = "得分")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double scored;

    @ApiModelProperty(value = "实验报告小题实例ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testExerciseInstanceId;

    public Double getScored() {
        return scored;
    }

    public void setScored(Double scored) {
        this.scored = scored;
    }

    public Integer getTestExerciseInstanceId() {
        return testExerciseInstanceId;
    }

    public void setTestExerciseInstanceId(Integer testExerciseInstanceId) {
        this.testExerciseInstanceId = testExerciseInstanceId;
    }
}
