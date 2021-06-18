package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * 实验报告小题实例
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_test_exercise_instance")
@ApiModel(value = "实验报告小题实例表")
@Data
public class TestExerciseInstance implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value = "习题ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer exerciseId;

    @Column(length = 10)
    @ApiModelProperty(value = "小题模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer testExerciseTemplateId;

    @Lob
    @ApiModelProperty(value = "习题内容")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String exerciseContent;

    @Column(length = 10)
    @ApiModelProperty(value = "显示类型 1-文字 2-图片")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer displayType;

    @Column(length = 10)
    @ApiModelProperty(value = "类型 1-客观题 2-主观题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnore
    private Integer type;

    @Column(length = 10)
    @ApiModelProperty(value = "题目类型 1-单选题 2-多选题 3-填空题 4-判断题 5-问答题 6-计算题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer exercisesType;

    @Lob
    @ApiModelProperty(value = "选项JSON:[{'displayOrder':'A','content':'2','order':'B'},{'displayOrder':'B','content':'1','order':'A'},{'displayOrder':'C','content':'4','order':'D'},{'displayOrder':'D','content':'3','order':'C'}]")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String options;

    @Lob
    @ApiModelProperty(value = "正确答案")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String correctAnswer;

    @Lob
    @ApiModelProperty(value = "答案")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String answer;

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告实例ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer testInstanceId;

    @Column(length = 10)
    @ApiModelProperty(value = "分项实例ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testSubsectionInstanceId;

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告大题实例ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testPartInstanceId;

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
    @ApiModelProperty(value = "状态 1-正确 0-错误")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

    @Column(length = 500)
    @ApiModelProperty(value = "班级")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String class1;

    @Column(length = 500)
    @ApiModelProperty(value = "年级")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String grade;

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
