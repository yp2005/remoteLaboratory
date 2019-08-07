package com.remoteLaboratory.entities;

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
 * 测验实例
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_test_instance")
@ApiModel(value = "测验实例表")
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

    @Column(length = 10)
    @ApiModelProperty(value = "所属章ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer chapterId;

    @Column(length = 255)
    @ApiModelProperty(value = "所属章名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String chapterName;

    @Column(length = 255)
    @ApiModelProperty(value = "所属章标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String chapterTitle;

    @Column(length = 10)
    @ApiModelProperty(value = "所属节ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer sectionId;

    @Column(length = 255)
    @ApiModelProperty(value = "所属节名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String sectionName;

    @Column(length = 255)
    @ApiModelProperty(value = "所属节标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String sectionTitle;

    @Column(length = 10)
    @ApiModelProperty(value = "测验人ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    @Column(length = 255)
    @ApiModelProperty(value = "测验人名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    @Column(length = 10)
    @ApiModelProperty(value = "当前答题位置-小题ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testExerciseInstanceId;

    @Column(length = 10)
    @ApiModelProperty(value = "测验模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer testTemplateId;

    @Column(length = 10)
    @ApiModelProperty(value = "状态 0-测验中 1-已提交 2-已阅卷")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTestExerciseInstanceId() {
        return testExerciseInstanceId;
    }

    public void setTestExerciseInstanceId(Integer testExerciseInstanceId) {
        this.testExerciseInstanceId = testExerciseInstanceId;
    }

    public Double getScored() {
        return scored;
    }

    public void setScored(Double scored) {
        this.scored = scored;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTestTemplateId() {
        return testTemplateId;
    }

    public void setTestTemplateId(Integer testTemplateId) {
        this.testTemplateId = testTemplateId;
    }

    public interface Validation{};
}
