package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 设备
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_device")
@ApiModel(value = "设备表")
public class Device {
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

    @Column(length = 10)
    @ApiModelProperty(value = "类型 1-在线实验设备 2-实时数据设备")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer type;

    @Column(length = 255)
    @ApiModelProperty(value = "和第三方系统资源进行关联的key")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String relationKey;

    @Column(length = 255)
    @ApiModelProperty(value = "在线实验设备需要嵌入的实验页面url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pageUrl;

    @Column(length = 255)
    @ApiModelProperty(value = "数据类型：实时数据设备根据数据类型使用不同的展示页面")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dataType;

    @Column(length = 255)
    @ApiModelProperty(value = "资源类型 如：示波器")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String resourceClass;

    @Column(updatable = false)
    @ApiModelProperty(value = "创建时间",hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createTime;

    @ApiModelProperty(value = "修改时间",hidden = true)
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getResourceClass() {
        return resourceClass;
    }

    public void setResourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
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

    public String getRelationKey() {
        return relationKey;
    }

    public void setRelationKey(String relationKey) {
        this.relationKey = relationKey;
    }

    public interface Validation{}
}
