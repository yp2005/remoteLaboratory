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
 * 讨论区-话题
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_subject")
@ApiModel(value = "话题表")
public class Subject implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String title;

    @Lob
    @ApiModelProperty(value = "内容")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String content;

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
    @ApiModelProperty(value = "作者ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    @Column(length = 255)
    @ApiModelProperty(value = "作者名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    @Lob
    @ApiModelProperty(value = "用户头像")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userImage;

    @Column(length = 255)
    @ApiModelProperty(value = "用户类型 1-admin 2-老师 3-学生")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userType;

    @Column(length = 10)
    @ApiModelProperty(value = "回复数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer replyNumber;

    @Column(length = 10)
    @ApiModelProperty(value = "排序号，优先使用此字段排序，置顶主题时设为0，默认为99")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer orderNumber;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getReplyNumber() {
        return replyNumber;
    }

    public void setReplyNumber(Integer replyNumber) {
        this.replyNumber = replyNumber;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public interface Validation{};
}
