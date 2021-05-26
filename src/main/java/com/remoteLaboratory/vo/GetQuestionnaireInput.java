package com.remoteLaboratory.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询问卷调查入参
 *
 * @Author: yupeng
 */
@ApiModel(value = "查询问卷调查入参")
@Data
public class GetQuestionnaireInput {
    @ApiModelProperty(value = "课程ID")
    private Integer courseId;

    @ApiModelProperty(value = "班级")
    private String class1;

    @ApiModelProperty(value = "年级")
    private String grade;
}
