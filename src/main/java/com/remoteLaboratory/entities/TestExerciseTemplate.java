package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 测验小题模板
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_test_exercise_template")
@ApiModel(value = "测验小题模板表")
public class TestExerciseTemplate implements Serializable {
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

    @Column(length = 5000)
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
    @ApiModelProperty(value = "选项 选择题填此字段JSON:[{'A': '1'},{'B': '2'}]")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String options;

    @Lob
    @ApiModelProperty(value = "正确答案 单选题-A 多选题-A,B,C 填空题-答案 判断题同选择题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String answer;

    @Column(length = 10)
    @ApiModelProperty(value = "测验模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer testTemplateId;
    
    @Column(length = 10)
    @ApiModelProperty(value = "测验大题模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer testPartTemplateId;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getTestPartTemplateId() {
        return testPartTemplateId;
    }

    public void setTestPartTemplateId(Integer testPartTemplateId) {
        this.testPartTemplateId = testPartTemplateId;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public interface Validation{};

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getExercisesType() {
        return exercisesType;
    }

    public void setExercisesType(Integer exercisesType) {
        this.exercisesType = exercisesType;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getExerciseContent() {
        return exerciseContent;
    }

    public void setExerciseContent(String exerciseContent) {
        this.exerciseContent = exerciseContent;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getTestTemplateId() {
        return testTemplateId;
    }

    public Integer getDisplayType() {
        return displayType;
    }

    public void setDisplayType(Integer displayType) {
        this.displayType = displayType;
    }

    public void setTestTemplateId(Integer testTemplateId) {
        this.testTemplateId = testTemplateId;
    }
}
