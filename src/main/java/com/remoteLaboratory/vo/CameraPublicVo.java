package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.Camera;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 摄像头公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "摄像头公用对象")
public class CameraPublicVo extends Camera {
    @ApiModelProperty(value = "srsUrl")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String srsUrl;

    @ApiModelProperty(value = "srsUniqueKey")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String srsUniqueKey;

    @ApiModelProperty(value = "srs服务名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String srsServerName;

    public CameraPublicVo() {

    }

    public CameraPublicVo(Camera camera) {
        this.setId(camera.getId());
        this.setCreateTime(camera.getCreateTime());
        this.setIp(camera.getIp());
        this.setName(camera.getName());
        this.setSrsServerId(camera.getSrsServerId());
        this.setUniqueKey(camera.getUniqueKey());
        this.setUpdateTime(camera.getUpdateTime());
        this.setUserName(camera.getUserName());
        this.setPassword(camera.getPassword());
        this.setVideoPort(camera.getVideoPort());
        this.setFindPort(camera.getFindPort());
        this.setControlPort(camera.getControlPort());
        this.setProtocol(camera.getProtocol());
        this.setRetransmitPort(camera.getRetransmitPort());
        this.setForceTcp(camera.getForceTcp());
        this.setBindStatus(camera.getBindStatus());
    }

    public Camera voToEntity() {
        Camera camera = new Camera();
        if(this.getId() != null) {
            camera.setId(this.getId());
        }
        camera.setCreateTime(this.getCreateTime());
        camera.setIp(this.getIp());
        camera.setName(this.getName());
        camera.setSrsServerId(this.getSrsServerId());
        camera.setUniqueKey(this.getUniqueKey());
        camera.setUpdateTime(this.getUpdateTime());
        camera.setUserName(this.getUserName());
        camera.setPassword(this.getPassword());
        camera.setVideoPort(this.getVideoPort());
        camera.setFindPort(this.getFindPort());
        camera.setControlPort(this.getControlPort());
        camera.setProtocol(this.getProtocol());
        camera.setRetransmitPort(this.getRetransmitPort());
        camera.setForceTcp(this.getForceTcp());
        camera.setBindStatus(this.getBindStatus());
        return camera;
    }

    public String getSrsUrl() {
        return srsUrl;
    }

    public void setSrsUrl(String srsUrl) {
        this.srsUrl = srsUrl;
    }

    public String getSrsServerName() {
        return srsServerName;
    }

    public void setSrsServerName(String srsServerName) {
        this.srsServerName = srsServerName;
    }

    public String getSrsUniqueKey() {
        return srsUniqueKey;
    }

    public void setSrsUniqueKey(String srsUniqueKey) {
        this.srsUniqueKey = srsUniqueKey;
    }
}
