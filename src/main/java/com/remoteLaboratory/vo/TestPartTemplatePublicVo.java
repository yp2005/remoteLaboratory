package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.TestExerciseTemplate;
import com.remoteLaboratory.entities.TestPartTemplate;
import com.remoteLaboratory.entities.TestPartTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 测验大题模板公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "测验大题模板公用对象")
public class TestPartTemplatePublicVo extends TestPartTemplate {
    @ApiModelProperty(value = "测验小题模板列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestExerciseTemplate> testExerciseTemplateList;

    public TestPartTemplatePublicVo() {

    }

    public TestPartTemplatePublicVo(TestPartTemplate testPartTemplate) {
        this.setId(testPartTemplate.getId());
        this.setCreateTime(testPartTemplate.getCreateTime());
        this.setName(testPartTemplate.getName());
        this.setUpdateTime(testPartTemplate.getUpdateTime());
        this.setDescription(testPartTemplate.getDescription());
        this.setScore(testPartTemplate.getScore());
        this.setSerialNumber(testPartTemplate.getSerialNumber());
        this.setType(testPartTemplate.getType());
        this.setTestTemplateId(testPartTemplate.getTestTemplateId());
    }

    public TestPartTemplate voToEntity() {
        TestPartTemplate testPartTemplate = new TestPartTemplate();
        if(this.getId() != null) {
            testPartTemplate.setId(this.getId());
        }
        testPartTemplate.setCreateTime(this.getCreateTime());
        testPartTemplate.setName(this.getName());
        testPartTemplate.setUpdateTime(this.getUpdateTime());
        testPartTemplate.setDescription(this.getDescription());
        testPartTemplate.setScore(this.getScore());
        testPartTemplate.setSerialNumber(this.getSerialNumber());
        testPartTemplate.setType(this.getType());
        testPartTemplate.setTestTemplateId(this.getTestTemplateId());
        return testPartTemplate;
    }

    public List<TestExerciseTemplate> getTestExerciseTemplateList() {
        return testExerciseTemplateList;
    }

    public void setTestExerciseTemplateList(List<TestExerciseTemplate> testExerciseTemplateList) {
        this.testExerciseTemplateList = testExerciseTemplateList;
    }
}
