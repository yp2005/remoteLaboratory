package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.TestSubsectionInstance;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分项实例公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "分项实例公用对象")
public class TestSubsectionInstancePublicVo extends TestSubsectionInstance {
    @ApiModelProperty(value = "实验报告大题实例列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestPartInstancePublicVo> testPartInstancePublicVoList;

    public TestSubsectionInstancePublicVo() {

    }

    public TestSubsectionInstancePublicVo(TestSubsectionTemplatePublicVo testSubsectionTemplatePublicVo) {
        this.setName(testSubsectionTemplatePublicVo.getName());
        this.setType(testSubsectionTemplatePublicVo.getType());
        this.setDescription(testSubsectionTemplatePublicVo.getDescription());
        this.setScore(testSubsectionTemplatePublicVo.getScore());
        this.setSerialNumber(testSubsectionTemplatePublicVo.getSerialNumber());
        this.setScored(0.0);
        List<TestPartTemplatePublicVo> testPartTemplatePublicVoList = testSubsectionTemplatePublicVo.getTestPartTemplatePublicVoList();
        if(CollectionUtils.isNotEmpty(testPartTemplatePublicVoList)) {
            this.testPartInstancePublicVoList = new ArrayList<>();
            for(TestPartTemplatePublicVo testPartTemplatePublicVo : testPartTemplatePublicVoList) {
                TestPartInstancePublicVo testPartInstancePublicVo = new TestPartInstancePublicVo(testPartTemplatePublicVo);
                this.testPartInstancePublicVoList.add(testPartInstancePublicVo);
            }
        }
    }

    public TestSubsectionInstancePublicVo(TestSubsectionInstance testSubsectionInstance) {
        this.setId(testSubsectionInstance.getId());
        this.setCreateTime(testSubsectionInstance.getCreateTime());
        this.setName(testSubsectionInstance.getName());
        this.setType(testSubsectionInstance.getType());
        this.setUpdateTime(testSubsectionInstance.getUpdateTime());
        this.setDescription(testSubsectionInstance.getDescription());
        this.setScore(testSubsectionInstance.getScore());
        this.setSerialNumber(testSubsectionInstance.getSerialNumber());
        this.setTestInstanceId(testSubsectionInstance.getTestInstanceId());
        this.setScored(testSubsectionInstance.getScored());
    }

    public TestSubsectionInstance voToEntity() {
        TestSubsectionInstance testSubsectionInstance = new TestSubsectionInstance();
        if(this.getId() != null) {
            testSubsectionInstance.setId(this.getId());
        }
        testSubsectionInstance.setCreateTime(this.getCreateTime());
        testSubsectionInstance.setName(this.getName());
        testSubsectionInstance.setType(this.getType());
        testSubsectionInstance.setUpdateTime(this.getUpdateTime());
        testSubsectionInstance.setDescription(this.getDescription());
        testSubsectionInstance.setScore(this.getScore());
        testSubsectionInstance.setSerialNumber(this.getSerialNumber());
        testSubsectionInstance.setTestInstanceId(this.getTestInstanceId());
        testSubsectionInstance.setScored(this.getScored());
        return testSubsectionInstance;
    }

    public List<TestPartInstancePublicVo> getTestPartInstancePublicVoList() {
        return testPartInstancePublicVoList;
    }

    public void setTestPartInstancePublicVoList(List<TestPartInstancePublicVo> testPartInstancePublicVoList) {
        this.testPartInstancePublicVoList = testPartInstancePublicVoList;
    }
}
