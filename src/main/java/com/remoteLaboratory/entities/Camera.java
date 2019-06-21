package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 摄像头
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_camera")
@ApiModel(value = "摄像头表")
public class Camera {
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

    @Column(length = 255)
    @ApiModelProperty(value = "ip地址")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String ip;

    @Column(length = 10)
    @ApiModelProperty(value = "视频端口")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer videoPort;

    @Column(length = 10)
    @ApiModelProperty(value = "发现端口")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer findPort;

    @Column(length = 10)
    @ApiModelProperty(value = "控制端口")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer controlPort;

    @Column(length = 10)
    @ApiModelProperty(value = "转发端口")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer retransmitPort;

    @Column(length = 255)
    @ApiModelProperty(value = "唯一标识")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String uniqueKey;

    @Column(length = 255)
    @ApiModelProperty(value = "协议类型")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String protocol;

    @Column(length = 255)
    @ApiModelProperty(value = "用户名")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    @Column(length = 255)
    @ApiModelProperty(value = "密码")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @Column(length = 10)
    @ApiModelProperty(value = "所属srs服务id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer srsServerId;

    @Column(length = 10)
    @ApiModelProperty(value = "关联设备ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer deviceId;

    @Column(length = 10)
    @ApiModelProperty(value = "是否强制rtsp一开始就使用tcp方式 0-否 1-是")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer forceTcp;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getVideoPort() {
        return videoPort;
    }

    public void setVideoPort(Integer videoPort) {
        this.videoPort = videoPort;
    }

    public Integer getFindPort() {
        return findPort;
    }

    public void setFindPort(Integer findPort) {
        this.findPort = findPort;
    }

    public Integer getControlPort() {
        return controlPort;
    }

    public void setControlPort(Integer controlPort) {
        this.controlPort = controlPort;
    }

    public Integer getRetransmitPort() {
        return retransmitPort;
    }

    public void setRetransmitPort(Integer retransmitPort) {
        this.retransmitPort = retransmitPort;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSrsServerId() {
        return srsServerId;
    }

    public void setSrsServerId(Integer srsServerId) {
        this.srsServerId = srsServerId;
    }

    public Integer getForceTcp() {
        return forceTcp;
    }

    public void setForceTcp(Integer forceTcp) {
        this.forceTcp = forceTcp;
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

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public interface Validation{}
}
