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
 * 小节学习记录
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_section_study_record")
@ApiModel(value = "小节学习记录表")
@Data
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

    @Column(length = 10)
    @ApiModelProperty(value = "学习状态 0-未学习 1-已学习 2-学习中")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer studyStatus;

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
