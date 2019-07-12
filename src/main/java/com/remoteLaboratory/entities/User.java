package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_user")
@ApiModel(value = "用户表")
public class User implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "用户ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "姓名或昵称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String personName;

    @Column(length = 255)
    @ApiModelProperty(value = "用户账号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String userName;

    @Column(length = 255)
    @ApiModelProperty(value = "用户联系电话")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String telphone;

    @Column(length = 255)
    @ApiModelProperty(value = "性别")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sex;

    @Column(length = 255)
    @ApiModelProperty(value = "民族")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String minzu;

    @Column(length = 255)
    @ApiModelProperty(value = "籍贯")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nativePlace;

    @Column(length = 255)
    @ApiModelProperty(value = "学院")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String college;

    @Column(length = 255)
    @ApiModelProperty(value = "专业")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String major;

    @Column(length = 255)
    @ApiModelProperty(value = "年级")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String grade;

    @Column(length = 255)
    @ApiModelProperty(value = "班")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String class1;

    @Column(length = 255)
    @ApiModelProperty(value = "用户标识，可能是身份证号、学号、教师编号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userKey;

    @Lob
    @ApiModelProperty(value = "用户头像")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userImage;

    @Column(length = 255)
    @ApiModelProperty(value = "用户类型 1-admin 2-老师 3-学生")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String userType;

    @Column(length = 255)
    @ApiModelProperty(value = "用户密码")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String password;

    @Column(length = 255)
    @ApiModelProperty(value = "token 不会入库，登陆返回用户信息时返回")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String token;

    @ApiModelProperty(value = "最后登录时间")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    @Column(length = 10, columnDefinition = "int default 0")
    @ApiModelProperty(value = "论坛禁言状态 0-正常 1-禁言")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer forumForbidden ;

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

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMinzu() {
        return minzu;
    }

    public void setMinzu(String minzu) {
        this.minzu = minzu;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    public Integer getForumForbidden() {
        return forumForbidden;
    }

    public void setForumForbidden(Integer forumForbidden) {
        this.forumForbidden = forumForbidden;
    }

    public interface Validation{};
}
