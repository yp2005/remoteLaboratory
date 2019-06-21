package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.Camera;
import com.remoteLaboratory.entities.SrsServer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * srs服务代理服务使用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "srs服务公用对象")
public class SrsServerAgentVo extends SrsServer {
    @ApiModelProperty(value = "下属摄像头")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Camera> cameraList;

    public SrsServerAgentVo() {

    }

    public SrsServerAgentVo(SrsServer srsServer) {
        this.setId(srsServer.getId());
        this.setCreateTime(srsServer.getCreateTime());
        this.setIp(srsServer.getIp());
        this.setName(srsServer.getName());
        this.setUniqueKey(srsServer.getUniqueKey());
        this.setUpdateTime(srsServer.getUpdateTime());
        this.setPort(srsServer.getPort());
        this.setUrl(srsServer.getUrl());
        this.setFileKeepTime(srsServer.getFileKeepTime());
        this.setRtmpPort(srsServer.getRtmpPort());
    }

    public SrsServer voToEntity() {
        SrsServer srsServer = new SrsServer();
        if(this.getId() != null) {
            srsServer.setId(this.getId());
        }
        srsServer.setCreateTime(this.getCreateTime());
        srsServer.setIp(this.getIp());
        srsServer.setName(this.getName());
        srsServer.setUniqueKey(this.getUniqueKey());
        srsServer.setUpdateTime(this.getUpdateTime());
        srsServer.setPort(this.getPort());
        srsServer.setUrl(this.getUrl());
        srsServer.setFileKeepTime(this.getFileKeepTime());
        srsServer.setRtmpPort(this.getRtmpPort());
        return srsServer;
    }

    public List<Camera> getCameraList() {
        return cameraList;
    }

    public void setCameraList(List<Camera> cameraList) {
        this.cameraList = cameraList;
    }
}
