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
 * 课程节
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_section")
@ApiModel(value = "课程节表")
@Data
public class Section implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "名称 如:第一节")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String name;

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

    @Column(length = 255)
    @ApiModelProperty(value = "视频URL")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String video;

    @Column(length = 255)
    @ApiModelProperty(value = "学习文档PDF")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String docPdf;

    @Column(length = 255)
    @ApiModelProperty(value = "学习课件PDF")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String coursewarePdf;

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
