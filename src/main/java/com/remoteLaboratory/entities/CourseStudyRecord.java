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
 * 课程学习记录
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_course_study_record")
@ApiModel(value = "课程学习记录表")
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

    public interface Validation{};
}
