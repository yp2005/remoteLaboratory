package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * 实验报告实例
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_test_instance")
@ApiModel(value = "实验报告实例表")
@Data
public class TestInstance implements Serializable {
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
    @ApiModelProperty(value = "试卷类型 1-实验报告 2-问卷调查")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testType;

    @Column(length = 4000)
    @ApiModelProperty(value = "说明")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @Column(length = 20)
    @ApiModelProperty(value = "总分")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Double score;

    @Column(length = 20)
    @ApiModelProperty(value = "得分")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Double scored;

    @Column(length = 10)
    @ApiModelProperty(value = "所属课程ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer courseId;

    @Column(length = 255)
    @ApiModelProperty(value = "所属课程名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String courseName;

    @Column(length = 500)
    @ApiModelProperty(value = "班级")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String class1;

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告人ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    @Column(length = 255)
    @ApiModelProperty(value = "实验报告人名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    @Column(length = 10)
    @ApiModelProperty(value = "当前答题位置-小题ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testExerciseInstanceId;

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer testTemplateId;

    @Column(length = 10)
    @ApiModelProperty(value = "状态 0-答题中 1-已提交 2-已阅卷")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

    @Column
    @ApiModelProperty(value = "是否往期")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Boolean isOld;

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
