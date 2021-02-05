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
 * 实验报告分项实例
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_test_subsection_instance")
@ApiModel(value = "实验报告分项实例表")
@Data
public class TestSubsectionInstance implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "名称: 课前预习、实验操作、实验数据、数据分析、实验报告")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String name;

    @Column(length = 10)
    @ApiModelProperty(value = "类型: 1-课前预 2-实验操 3-实验数 4-数据分析 5-实验报告")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer type;

    @Column(length = 4000)
    @ApiModelProperty(value = "说明")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @Column(length = 10)
    @ApiModelProperty(value = "序号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer serialNumber;

    @Column(length = 20)
    @ApiModelProperty(value = "分数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Double score;

    @Column(length = 20)
    @ApiModelProperty(value = "得分")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double scored;

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告实例ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer testInstanceId;

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
