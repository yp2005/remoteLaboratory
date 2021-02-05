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
 * 章学习记录
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_chapter_study_record")
@ApiModel(value = "章学习记录表")
@Data
public class ChapterStudyRecord implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value = "章ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer chapterId;

    @Column(length = 255)
    @ApiModelProperty(value = "章名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String chapterName;

    @Column(length = 255)
    @ApiModelProperty(value = "章标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String chapterTitle;

    @Column(length = 10)
    @ApiModelProperty(value = "课程学习记录ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer courseStudyRecordId;

    @Column(length = 20)
    @ApiModelProperty(value = "已学习进度 如：0.5=50%")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double studied;

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

    public interface Validation{}
}
