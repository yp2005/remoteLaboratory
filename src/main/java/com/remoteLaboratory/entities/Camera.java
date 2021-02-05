package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
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
@Data
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

    @Column(length = 255)
    @ApiModelProperty("视频路径")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String videoPath;

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
    @ApiModelProperty(value = "是否强制rtsp一开始就使用tcp方式 0-否 1-是")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer forceTcp;

    @Column(length = 10)
    @ApiModelProperty(value = "是否被设备绑定 0-否 1-是")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer bindStatus;

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

    public interface Validation{}
}
