package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;

/**
 * 答题入参
 *
 * @Author: yupeng
 */
@ApiModel(value = "答题入参")
public class AnswerInput {
    @ApiModelProperty(value = "答案")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String answer;

    @ApiModelProperty(value = "实验报告小题实例ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testExerciseInstanceId;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getTestExerciseInstanceId() {
        return testExerciseInstanceId;
    }

    public void setTestExerciseInstanceId(Integer testExerciseInstanceId) {
        this.testExerciseInstanceId = testExerciseInstanceId;
    }
}
