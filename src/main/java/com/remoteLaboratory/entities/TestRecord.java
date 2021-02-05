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
 * 实验报告记录
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_test_record")
@ApiModel(value = "实验报告记录")
@Data
public class TestRecord implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value = "课程学习记录ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer courseStudyRecordId;

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer testTemplateId;

    @Column(length = 255)
    @ApiModelProperty(value = "实验报告模板名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String testTemplateName;

    @Column(length = 10)
    @ApiModelProperty(value = "状态 0-答题中 1-已提交 2-已阅卷")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告实例ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testInstanceId;

    @Column(length = 10)
    @ApiModelProperty(value = "用户ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    @Column(length = 255)
    @ApiModelProperty(value = "用户名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

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
