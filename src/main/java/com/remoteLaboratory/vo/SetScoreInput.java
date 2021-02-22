package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设置课程分数分布入参
 *
 * @Author: yupeng
 */
@ApiModel(value = "设置课程分数分布入参")
@Data
public class SetScoreInput {
    @ApiModelProperty(value = "课程ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer courseId;

    @ApiModelProperty(value = "课前预习分数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double preStudyScore;

    @ApiModelProperty(value = "实验报告分数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double reportScore;
}
