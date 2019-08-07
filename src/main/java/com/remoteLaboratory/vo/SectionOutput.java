package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.Section;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 小节列表输出
 *
 * @Author: yupeng
 */
@ApiModel(value = "小节列表输出")
public class SectionOutput {
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

    @ApiModelProperty(value = "名称 如:第一节")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @ApiModelProperty(value = "标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;

    @ApiModelProperty(value = "所属课程ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer courseId;

    @ApiModelProperty(value = "所属课程名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String courseName;

    @ApiModelProperty(value = "所属章ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer chapterId;

    @Column(length = 255)
    @ApiModelProperty(value = "所属章名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String chapterName;

    @ApiModelProperty(value = "所属章标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String chapterTitle;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date createTime;

    @ApiModelProperty(value = "修改时间", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date updateTime;

    public SectionOutput(Section section) {
        this.id = section.getId();
        this.chapterId = section.getChapterId();
        this.chapterName = section.getChapterName();
        this.chapterTitle = section.getChapterTitle();
        this.courseId = section.getCourseId();
        this.courseName = section.getCourseName();
        this.createTime = section.getCreateTime();
        this.updateTime = section.getUpdateTime();
        this.name = section.getName();
        this.title = section.getTitle();
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
