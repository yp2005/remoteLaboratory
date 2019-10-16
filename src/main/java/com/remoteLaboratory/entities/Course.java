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
 * 课程
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_course")
@ApiModel(value = "课程表")
public class Course implements Serializable {
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

    @Lob
    @ApiModelProperty(value = "logo(首页显示)")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String logo;

    @Lob
    @ApiModelProperty(value = "主图(列表、详情、推荐等显示)")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String mainImg;

    @Column(length = 255)
    @ApiModelProperty(value = "课程视频介绍")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String videoDesc;

    @Column(length = 10)
    @ApiModelProperty(value = "老师ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer teacherId;

    @Column(length = 255)
    @ApiModelProperty(value = "老师名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String teacherName;

    @Lob
    @ApiModelProperty(value = "评分标准")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String standard;

    @Column(length = 2000)
    @ApiModelProperty(value = "简介")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String introduction;

    @Lob
    @ApiModelProperty(value = "描述")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @Column(length = 10)
    @ApiModelProperty(value = "状态 0-草稿(即将开始) 1-发布(进行中) 2-结束")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

    @Column(length = 10)
    @ApiModelProperty(value = "已参加学习人数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer studentNumber;

    @Column(length = 10)
    @ApiModelProperty(value = "评价数(讨论区主题数量)")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer subjectNumber;

    @Column(length = 10)
    @ApiModelProperty(value = "实验总时长")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer timeLimit;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Integer getSubjectNumber() {
        return subjectNumber;
    }

    public void setSubjectNumber(Integer subjectNumber) {
        this.subjectNumber = subjectNumber;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public interface Validation{};
}
