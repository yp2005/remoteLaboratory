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
 * 实验报告大题模板
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_test_part_template")
@ApiModel(value = "实验报告大题模板表")
@Data
public class TestPartTemplate implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "名称 如 一、单选题 2、多选题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String name;

    @Column(length = 10)
    @ApiModelProperty(value = "题目类型 1-单选题 2-多选题 3-填空题 4-判断题 5-问答题 6-计算题")
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

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer testTemplateId;

    @Column(length = 10)
    @ApiModelProperty(value = "分项模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer testSubsectionTemplateId;

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
