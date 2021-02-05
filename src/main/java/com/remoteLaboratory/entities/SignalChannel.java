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
 * 信号通道
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_signal_channel")
@ApiModel(value = "信号通道表")
@Data
public class SignalChannel {
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

    @Lob
    @ApiModelProperty(value = "图片")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String picture;

    @Column(length = 255)
    @ApiModelProperty(value = "信道编号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String channelId;

    @Column(length = 255)
    @ApiModelProperty(value = "数据类型：实时数据信号通道根据数据类型使用不同的展示页面")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dataType;

    @Column(length = 10)
    @ApiModelProperty(value = "所属设备ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer deviceId;

    @Column(length = 255)
    @ApiModelProperty(value = "所属设备名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deviceName;

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

    public interface Validation{}
}
