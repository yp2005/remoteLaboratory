package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.Camera;
import com.remoteLaboratory.entities.CourseDevice;
import com.remoteLaboratory.entities.Device;
import com.remoteLaboratory.entities.SignalChannel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

/**
 * 课程设备输出对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "课程设备输出对象")
public class CourseDeviceOutputVo extends CourseDevice {
    @ApiModelProperty(value = "和第三方系统资源进行关联的key")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String relationKey;

    public String getRelationKey() {
        return relationKey;
    }

    public void setRelationKey(String relationKey) {
        this.relationKey = relationKey;
    }

    public List<SignalChannel> getSignalChannelList() {
        return signalChannelList;
    }

    public void setSignalChannelList(List<SignalChannel> signalChannelList) {
        this.signalChannelList = signalChannelList;
    }

    @ApiModelProperty(value = "信号通道集合")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SignalChannel> signalChannelList;

    public CourseDeviceOutputVo() {

    }

    public CourseDeviceOutputVo(CourseDevice courseDevice) {
        this.setId(courseDevice.getId());
        this.setCreateTime(courseDevice.getCreateTime());
        this.setUpdateTime(courseDevice.getUpdateTime());
        this.setCourseId(courseDevice.getCourseId());
        this.setCourseName(courseDevice.getCourseName());
        this.setDeviceId(courseDevice.getDeviceId());
        this.setDeviceName(courseDevice.getDeviceName());
        this.setExperimenteName(courseDevice.getExperimenteName());
        this.setPicture(courseDevice.getPicture());
        this.setResourceClass(courseDevice.getResourceClass());
        this.setType(courseDevice.getType());
    }


}
