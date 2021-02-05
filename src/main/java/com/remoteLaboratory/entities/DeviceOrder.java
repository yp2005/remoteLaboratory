package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 设备预约
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_device_order")
@ApiModel(value = "设备预约表")
@Data
public class DeviceOrder implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value = "设备Id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer deviceId;

    @Column(length = 255)
    @ApiModelProperty(value = "设备名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String deviceName;

    @Column(length = 10)
    @ApiModelProperty(value = "课程Id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer courseId;

    @Column(length = 255)
    @ApiModelProperty(value = "课程名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String courseName;

    @Column(length = 255)
    @ApiModelProperty(value = "实验名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String experimenteName;

    @Column(length = 10)
    @ApiModelProperty(value = "用户ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    @Column(length = 255)
    @ApiModelProperty(value = "用户名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    @ApiModelProperty(value = "开始时间", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @ApiModelProperty(value = "开始时间", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(length = 10)
    @ApiModelProperty(value = "年")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer year;

    @Column(length = 10)
    @ApiModelProperty(value = "月")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer month;

    @Column(length = 10)
    @ApiModelProperty(value = "日")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer day;

    @Column(length = 10)
    @ApiModelProperty(value = "开始时")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer startHour;

    @Column(length = 10)
    @ApiModelProperty(value = "结束时")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer endHour;

    @Column(length = 10)
    @ApiModelProperty(value = "状态 0-未被预约 1-已被预约")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

    @Column(updatable = false)
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createTime;

    @ApiModelProperty(value = "修改时间", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateTime;

    public interface Validation{}
}
