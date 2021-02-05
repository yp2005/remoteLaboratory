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
 * 课程设备(只能选择在线实验设备)
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_course_device")
@ApiModel(value = "课程设备表")
@Data
public class CourseDevice implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "实验名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String experimenteName;

    @Column(length = 10)
    @ApiModelProperty(value = "课程Id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer courseId;

    @Column(length = 255)
    @ApiModelProperty(value = "课程名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String courseName;

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

    @Column(length = 255)
    @ApiModelProperty(value = "资源类型 如：示波器")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String resourceClass;

    @Column(length = 10)
    @ApiModelProperty(value = "类型 1-在线实验设备 2-实时数据设备")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer type;

    @Lob
    @ApiModelProperty(value = "图片")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String picture;

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
