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
 * 讨论区-话题
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_subject")
@ApiModel(value = "话题表")
@Data
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

    public interface Validation{}
}
