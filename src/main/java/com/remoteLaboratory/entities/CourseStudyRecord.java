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
 * 课程学习记录
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_course_study_record")
@ApiModel(value = "课程学习记录表")
@Data
public class CourseStudyRecord implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value = "课程ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer courseId;

    @Column(length = 255)
    @ApiModelProperty(value = "课程名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String courseName;

    @Lob
    @ApiModelProperty(value = "课程主图")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String courseMainImg;

    @Column(length = 255)
    @ApiModelProperty(value = "课程视频介绍")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String courseVideoDesc;

    @Column(length = 2000)
    @ApiModelProperty(value = "课程简介")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String courseIntroduction;

    @Column(length = 500)
    @ApiModelProperty(value = "班级")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String class1;

    @Column(length = 10)
    @ApiModelProperty(value = "用户ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    @Column(length = 255)
    @ApiModelProperty(value = "用户名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    @Column(length = 20)
    @ApiModelProperty(value = "已学习进度 如：0.5=50%")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double studied;

    @Column(length = 20)
    @ApiModelProperty(value = "课程得分")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Double score;

    @Column(length = 20)
    @ApiModelProperty(value = "课前预习得分")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Double preStudyScore;

    @Column(length = 20)
    @ApiModelProperty(value = "实验操作得分")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Double operationScore;

    @Column(length = 20)
    @ApiModelProperty(value = "实验数据得分")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Double dataScore;

    @Column(length = 20)
    @ApiModelProperty(value = "数据分析得分")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Double dataAnalysisScore;

    @Column(length = 20)
    @ApiModelProperty(value = "实验报告得分")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Double reportScore;

    @Column
    @ApiModelProperty(value = "是否已计算分数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Boolean graded;

    @Column(length = 10)
    @ApiModelProperty(value = "正在学习章ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer chapterId;

    @Column(length = 255)
    @ApiModelProperty(value = "正在学习章名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String chapterName;

    @Column(length = 255)
    @ApiModelProperty(value = "正在学习章标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String chapterTitle;

    @Column(length = 10)
    @ApiModelProperty(value = "正在学习节ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer sectionId;

    @Column(length = 255)
    @ApiModelProperty(value = "正在学习节名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sectionName;

    @Column(length = 255)
    @ApiModelProperty(value = "正在学习节标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sectionTitle;

    @Column(length = 10)
    @ApiModelProperty(value = "学习状态 0-学习中 1-已完成")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

    @Column
    @ApiModelProperty(value = "是否往期")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Boolean isOld;

    @Column
    @ApiModelProperty(value = "是否完成问卷调查")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isQuestionnaireFinish;

    @Column(length = 10)
    @ApiModelProperty(value = "问卷调查模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer questionnaireTemplateId;

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告数量")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testTemplateNumber;

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告阅卷完成数量")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testTemplateFinishedNumber;

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告已交卷数量")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testTemplateSubmitedNumber;

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
