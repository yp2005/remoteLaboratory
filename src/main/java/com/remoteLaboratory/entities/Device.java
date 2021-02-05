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
import java.util.List;

/**
 * 设备
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_device")
@ApiModel(value = "设备表")
@Data
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

    public interface Validation{}
}
