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
 * 实验报告模板
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_test_template")
@ApiModel(value = "实验报告模板表")
@Data
public class TestTemplate implements Serializable {
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
    @JsonIgnore
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
