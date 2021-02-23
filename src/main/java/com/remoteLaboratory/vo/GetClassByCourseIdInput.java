package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 根据courseId获取学习课程的班级入参
 *
 * @Author: yupeng
 */
@ApiModel(value = "根据courseId获取学习课程的班级入参")
@Data
public class GetClassByCourseIdInput {
    @ApiModelProperty(value = "课程ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer courseId;

    @ApiModelProperty(value = "类型 0-全部 1-当期 2-往期")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer type;
}
