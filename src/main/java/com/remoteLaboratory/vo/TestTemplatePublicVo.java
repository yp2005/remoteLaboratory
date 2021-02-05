package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.TestExerciseTemplate;
import com.remoteLaboratory.entities.TestTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 实验报告模板公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "实验报告模板公用对象")
public class TestTemplatePublicVo extends TestTemplate {
    @ApiModelProperty(value = "实验报告分项模板列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestSubsectionTemplatePublicVo> testSubsectionTemplatePublicVoList;

    @ApiModelProperty(value = "实验报告小题模板列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestExerciseTemplate> testExerciseTemplateList;

    public TestTemplatePublicVo() {

    }

    public TestTemplatePublicVo(TestTemplate testTemplate) {
        this.setId(testTemplate.getId());
        this.setCreateTime(testTemplate.getCreateTime());
        this.setName(testTemplate.getName());
        this.setUpdateTime(testTemplate.getUpdateTime());
        this.setCourseId(testTemplate.getCourseId());
        this.setCourseName(testTemplate.getCourseName());
        this.setDescription(testTemplate.getDescription());
        this.setScore(testTemplate.getScore());
        this.setTestType(testTemplate.getTestType());
    }

    public TestTemplate voToEntity() {
        TestTemplate testTemplate = new TestTemplate();
        if(this.getId() != null) {
            testTemplate.setId(this.getId());
        }
        testTemplate.setCreateTime(this.getCreateTime());
        testTemplate.setName(this.getName());
        testTemplate.setUpdateTime(this.getUpdateTime());
        testTemplate.setCourseId(this.getCourseId());
        testTemplate.setCourseName(this.getCourseName());
        testTemplate.setDescription(this.getDescription());
        testTemplate.setScore(this.getScore());
        testTemplate.setTestType(this.getTestType());
        return testTemplate;
    }

    public List<TestSubsectionTemplatePublicVo> getTestSubsectionTemplatePublicVoList() {
        return testSubsectionTemplatePublicVoList;
    }

    public void setTestSubsectionTemplatePublicVoList(List<TestSubsectionTemplatePublicVo> testSubsectionTemplatePublicVoList) {
        this.testSubsectionTemplatePublicVoList = testSubsectionTemplatePublicVoList;
    }

    public List<TestExerciseTemplate> getTestExerciseTemplateList() {
        return testExerciseTemplateList;
    }

    public void setTestExerciseTemplateList(List<TestExerciseTemplate> testExerciseTemplateList) {
        this.testExerciseTemplateList = testExerciseTemplateList;
    }
}
