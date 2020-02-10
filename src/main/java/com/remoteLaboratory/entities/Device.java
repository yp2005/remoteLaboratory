package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 设备
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_device")
@ApiModel(value = "设备表")
public class Device {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String name;

    @Column(length = 10)
    @ApiModelProperty(value = "类型 1-在线实验设备 2-实时数据设备")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer type;

    @Column(length = 10)
    @ApiModelProperty(value = "实验时长(设备预约时长) 单位：小时")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer duration;

    @Lob
    @ApiModelProperty(value = "图片")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String picture;

    @Column(length = 255)
    @ApiModelProperty(value = "和第三方系统资源进行关联的key")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String relationKey;

    @Column(length = 255)
    @ApiModelProperty(value = "websocket ip")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String websocketIp;

    @Column(length = 255)
    @ApiModelProperty(value = "websocket 端口")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String websocketPort;

    @Column(length = 255)
    @ApiModelProperty(value = "在线实验设备需要嵌入的实验页面url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pageUrl;

    @Column(length = 255)
    @ApiModelProperty(value = "资源类型 如：示波器")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String resourceClass;

    @Column(length = 10)
    @ApiModelProperty(value = "关联摄像头ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer cameraId;

    @Column(length = 255)
    @ApiModelProperty(value = "关联摄像头名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cameraName;

    @Column(length = 3000)
    @ApiModelProperty(value = "描述")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @Column(updatable = false)
    @ApiModelProperty(value = "创建时间",hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createTime;

    @ApiModelProperty(value = "修改时间",hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateTime;

    @Transient
    @ApiModelProperty(value = "信号通道集合")
    private List<SignalChannel> signalChannelList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getResourceClass() {
        return resourceClass;
    }

    public void setResourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRelationKey() {
        return relationKey;
    }

    public void setRelationKey(String relationKey) {
        this.relationKey = relationKey;
    }

    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<SignalChannel> getSignalChannelList() {
        return signalChannelList;
    }

    public void setSignalChannelList(List<SignalChannel> signalChannelList) {
        this.signalChannelList = signalChannelList;
    }

    public String getWebsocketIp() {
        return websocketIp;
    }

    public void setWebsocketIp(String websocketIp) {
        this.websocketIp = websocketIp;
    }

    public String getWebsocketPort() {
        return websocketPort;
    }

    public void setWebsocketPort(String websocketPort) {
        this.websocketPort = websocketPort;
    }

    public interface Validation{}
}
