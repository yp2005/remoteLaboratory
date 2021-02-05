package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统设置
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_sys_setting")
@ApiModel(value = "系统设置表")
@Data
public class SysSetting implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "参数中文名")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String zhName;

    @Column(length = 255)
    @ApiModelProperty(value = "参数英文标识")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String keyName;

    @Column(length = 3000)
    @ApiModelProperty(value = "参数值")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;

    @Column(length = 255)
    @ApiModelProperty(value = "说明")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

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
}
