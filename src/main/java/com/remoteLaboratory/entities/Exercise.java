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
 * 习题
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_exercise")
@ApiModel(value = "习题表")
@Data
public class Exercise implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 5000)
    @ApiModelProperty(value = "内容")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String content;

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
    @ApiModelProperty(value = "试卷类型 1-实验报告 2-问卷调查")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnore
    private Integer testType;

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
