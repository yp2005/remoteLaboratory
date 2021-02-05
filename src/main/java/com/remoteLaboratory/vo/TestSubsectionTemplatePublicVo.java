package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.TestSubsectionTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 分项模板公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "分项模板公用对象")
public class TestSubsectionTemplatePublicVo extends TestSubsectionTemplate {
    @ApiModelProperty(value = "实验报告大题模板列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestPartTemplatePublicVo> testPartTemplatePublicVoList;

    public TestSubsectionTemplatePublicVo() {

    }

    public TestSubsectionTemplatePublicVo(TestSubsectionTemplate testSubsectionTemplate) {
        this.setId(testSubsectionTemplate.getId());
        this.setCreateTime(testSubsectionTemplate.getCreateTime());
        this.setName(testSubsectionTemplate.getName());
        this.setType(testSubsectionTemplate.getType());
        this.setUpdateTime(testSubsectionTemplate.getUpdateTime());
        this.setDescription(testSubsectionTemplate.getDescription());
        this.setScore(testSubsectionTemplate.getScore());
        this.setSerialNumber(testSubsectionTemplate.getSerialNumber());
        this.setTestTemplateId(testSubsectionTemplate.getTestTemplateId());
    }

    public TestSubsectionTemplate voToEntity() {
        TestSubsectionTemplate testSubsectionTemplate = new TestSubsectionTemplate();
        if(this.getId() != null) {
            testSubsectionTemplate.setId(this.getId());
        }
        testSubsectionTemplate.setCreateTime(this.getCreateTime());
        testSubsectionTemplate.setName(this.getName());
        testSubsectionTemplate.setType(this.getType());
        testSubsectionTemplate.setUpdateTime(this.getUpdateTime());
        testSubsectionTemplate.setDescription(this.getDescription());
        testSubsectionTemplate.setScore(this.getScore());
        testSubsectionTemplate.setSerialNumber(this.getSerialNumber());
        testSubsectionTemplate.setTestTemplateId(this.getTestTemplateId());
        return testSubsectionTemplate;
    }

    public List<TestPartTemplatePublicVo> getTestPartTemplatePublicVoList() {
        return testPartTemplatePublicVoList;
    }

    public void setTestPartTemplatePublicVoList(List<TestPartTemplatePublicVo> testPartTemplatePublicVoList) {
        this.testPartTemplatePublicVoList = testPartTemplatePublicVoList;
    }
}
