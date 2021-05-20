package com.remoteLaboratory.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询问卷调查入参
 *
 * @Author: yupeng
 */
@ApiModel(value = "查询问卷调查入参")
public class GetQuestionnaireInput {
    @ApiModelProperty(value = "课程ID")
    private Integer courseId;

    @ApiModelProperty(value = "班级")
    private String class1;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }
}
