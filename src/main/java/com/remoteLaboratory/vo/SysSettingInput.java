package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;

/**
 * 系统设置入参
 *
 * @Author: yupeng
 */
@ApiModel(value = "系统设置入参")
public class SysSettingInput {
    @Column(length = 255)
    @ApiModelProperty(value = "参数英文标识")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String keyName;

    @Column(length = 255)
    @ApiModelProperty(value = "参数值")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
