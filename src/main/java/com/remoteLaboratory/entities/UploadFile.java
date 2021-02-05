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
 * 上传文件
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_upload_file")
@ApiModel(value = "上传文件表")
@Data
public class UploadFile implements Serializable {
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
    @ApiModelProperty(value = "上传后的文件路径和名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String uploadFileName;

    @Column(length = 10)
    @ApiModelProperty(value = "所属用户ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer userId;

    @Column(length = 255)
    @ApiModelProperty(value = "所属用户名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
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
