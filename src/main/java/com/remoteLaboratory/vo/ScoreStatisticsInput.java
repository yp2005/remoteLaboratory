package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分数分布统计入参
 *
 * @Author: yupeng
 */
@ApiModel(value = "分数分布统计入参")
@Data
public class ScoreStatisticsInput {
    @ApiModelProperty(value = "课程ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer courseId;

    @ApiModelProperty(value = "班级")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String class1;

    @ApiModelProperty(value = "类型 0-全部 1-当期 2-往期")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer type;
}
