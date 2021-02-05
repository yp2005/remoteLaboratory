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
 * 讨论区-回复
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_reply")
@ApiModel(value = "回复表")
@Data
public class Reply implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value = "类型 1-主题回复 2-回复的回复")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer type;

    @Lob
    @ApiModelProperty(value = "内容")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String content;

    @Column(length = 10)
    @ApiModelProperty(value = "主题ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer subjectId;

    @Column(length = 255)
    @ApiModelProperty(value = "主题标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String subjectTitle;

    @Column(length = 10)
    @ApiModelProperty(value = "回复ID(type = 2)")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer replayId;

    @Column(length = 10)
    @ApiModelProperty(value = "回复的人的ID(type = 2)")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer replyUserId;

    @Column(length = 255)
    @ApiModelProperty(value = "回复的人的名称(type = 2)")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String replyUserName;

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
    @ApiModelProperty(value = "楼层数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer floorNumber;

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
