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
 * 小节学习记录
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_section_study_record")
@ApiModel(value = "小节学习记录表")
public class SectionStudyRecord implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value = "章ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer chapterId;

    @Column(length = 255)
    @ApiModelProperty(value = "章名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String chapterName;

    @Column(length = 255)
    @ApiModelProperty(value = "章标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String chapterTitle;

    @Column(length = 10)
    @ApiModelProperty(value = "节ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer sectionId;

    @Column(length = 255)
    @ApiModelProperty(value = "节名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String sectionName;

    @Column(length = 255)
    @ApiModelProperty(value = "节标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String sectionTitle;

    @Column(length = 10)
    @ApiModelProperty(value = "课程学习记录ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer courseStudyRecordId;

    @Column(length = 10)
    @ApiModelProperty(value = "章学习记录ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer chapterStudyRecordId;

    @Column(length = 20)
    @ApiModelProperty(value = "已学习进度 如：0.5=50%")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double studied;

    @Column(length = 10)
    @ApiModelProperty(value = "学习状态 0-未学习 1-已学习 2-正在学习")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer studyStatus;

    @Column(length = 10)
    @ApiModelProperty(value = "作业状态 0-未完成 1-已完成 2-正在进行")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testStatus;

    @Column(length = 10)
    @ApiModelProperty(value = "节标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testInstanceId;

    @Column(length = 10)
    @ApiModelProperty(value = "用户ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    @Column(length = 255)
    @ApiModelProperty(value = "用户名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

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

    public Double getStudied() {
        return studied;
    }

    public void setStudied(Double studied) {
        this.studied = studied;
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

    public Integer getChapterStudyRecordId() {
        return chapterStudyRecordId;
    }

    public void setChapterStudyRecordId(Integer chapterStudyRecordId) {
        this.chapterStudyRecordId = chapterStudyRecordId;
    }

    public Integer getStudyStatus() {
        return studyStatus;
    }

    public void setStudyStatus(Integer studyStatus) {
        this.studyStatus = studyStatus;
    }

    public Integer getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(Integer testStatus) {
        this.testStatus = testStatus;
    }

    public Integer getCourseStudyRecordId() {
        return courseStudyRecordId;
    }

    public void setCourseStudyRecordId(Integer courseStudyRecordId) {
        this.courseStudyRecordId = courseStudyRecordId;
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

    public Integer getTestInstanceId() {
        return testInstanceId;
    }

    public void setTestInstanceId(Integer testInstanceId) {
        this.testInstanceId = testInstanceId;
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

    public interface Validation{};
}
