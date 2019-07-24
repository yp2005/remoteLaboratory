package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 绑定摄像头入参
 *
 * @Author: yupeng
 */
@ApiModel(value = "绑定摄像头入参")
public class BindCameraInput {
    @ApiModelProperty(value = "设备ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer deviceId;

    @ApiModelProperty(value = "摄像头ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer cameraId;

    @ApiModelProperty(value = "状态 1-绑定 0-取消绑定")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
