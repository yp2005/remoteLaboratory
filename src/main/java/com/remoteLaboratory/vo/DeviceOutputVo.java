package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.Camera;
import com.remoteLaboratory.entities.Device;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备输出对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "设备输出对象")
public class DeviceOutputVo extends Device {
    @ApiModelProperty(value = "绑定摄像头")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Camera camera;

    public DeviceOutputVo() {

    }

    public DeviceOutputVo(Device device) {
        this.setId(device.getId());
        this.setCreateTime(device.getCreateTime());
        this.setName(device.getName());
        this.setUpdateTime(device.getUpdateTime());
        this.setCameraId(device.getCameraId());
        this.setDataType(device.getDataType());
        this.setPageUrl(device.getPageUrl());
        this.setRelationKey(device.getRelationKey());
        this.setResourceClass(device.getResourceClass());
        this.setType(device.getType());
        this.setCameraName(device.getCameraName());
        this.setDescription(device.getDescription());
        this.setPicture(device.getPicture());
        this.setDuration(device.getDuration());
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
