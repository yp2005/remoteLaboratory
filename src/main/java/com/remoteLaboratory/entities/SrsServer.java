package com.remoteLaboratory.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * srs服务
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_srs_server")
@ApiModel(value = "srs服务表")
@Data
public class SrsServer {
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

    @Column(length = 255)
    @ApiModelProperty(value = "ip地址")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String ip;

    @Column(length = 10)
    @ApiModelProperty(value = "端口")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer port;

    @Column(length = 10)
    @ApiModelProperty(value = "rtmp端口")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer rtmpPort;

    @Column(length = 255)
    @ApiModelProperty(value = "唯一标识")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String uniqueKey;

    @Column(length = 10)
    @ApiModelProperty(value = "文件保留时间(天)")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer fileKeepTime;

    @Column(length = 255)
    @ApiModelProperty(value = "url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;

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

    public interface Validation{}
}
