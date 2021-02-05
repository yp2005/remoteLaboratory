package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 日志记录
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_log_record")
@ApiModel(value = "日志记录表")
@Data
public class LogRecord implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "操作类型-登陆 登出 查询 列表查询 修改 删除 添加等")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;

    @Column(length = 255)
    @ApiModelProperty(value = "操作对象")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String object;

    @Column(length = 10)
    @ApiModelProperty(value = "操作对象ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer objectId;

    @Column(length = 255)
    @ApiModelProperty(value = "操作对象名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String objectName;

    @Column(length = 10)
    @ApiModelProperty(value = "操作人ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    @Column(length = 255)
    @ApiModelProperty(value = "操作人名称")
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
