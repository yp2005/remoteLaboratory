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
 * 问卷调查统计
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_questionnaire_statistics")
@ApiModel(value = "问卷调查统计")
@Data
public class QuestionnaireStatistics implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value = "小题模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testExerciseTemplateId;

    @Column(length = 500)
    @ApiModelProperty(value = "班级")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String class1;

    @Column(length = 2)
    @ApiModelProperty(value = "选项")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String optionOrder;

    @Column(length = 10)
    @ApiModelProperty(value = "选择人数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer selectNumber;

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
