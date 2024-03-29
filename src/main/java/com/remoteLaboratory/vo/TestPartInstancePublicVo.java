package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.TestExerciseInstance;
import com.remoteLaboratory.entities.TestExerciseTemplate;
import com.remoteLaboratory.entities.TestPartInstance;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 实验报告大题实例公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "实验报告大题实例公用对象")
public class TestPartInstancePublicVo extends TestPartInstance {
    @ApiModelProperty(value = "实验报告小题实例列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List testExerciseInstanceList;

    public TestPartInstancePublicVo() {

    }

    public TestPartInstancePublicVo(TestPartTemplatePublicVo testPartTemplatePublicVo) {
        this.setName(testPartTemplatePublicVo.getName());
        this.setDescription(testPartTemplatePublicVo.getDescription());
        this.setScore(testPartTemplatePublicVo.getScore());
        this.setSerialNumber(testPartTemplatePublicVo.getSerialNumber());
        this.setType(testPartTemplatePublicVo.getType());
        this.setScored(0.0);
        List<TestExerciseTemplate> testExerciseTemplateList = testPartTemplatePublicVo.getTestExerciseTemplateList();
        if(CollectionUtils.isNotEmpty(testExerciseTemplateList)) {
            this.testExerciseInstanceList = new ArrayList<>();
            for(TestExerciseTemplate testExerciseTemplate : testExerciseTemplateList) {
                TestExerciseInstance testExerciseInstance = new TestExerciseInstance();
                testExerciseInstance.setExerciseContent(testExerciseTemplate.getExerciseContent());
                testExerciseInstance.setExerciseId(testExerciseTemplate.getExerciseId());
                testExerciseInstance.setTestExerciseTemplateId(testExerciseTemplate.getId());
                testExerciseInstance.setExercisesType(testExerciseTemplate.getExercisesType());
                testExerciseInstance.setOptions(testExerciseTemplate.getRandomOrderOptions());
                testExerciseInstance.setScore(testExerciseTemplate.getScore());
                testExerciseInstance.setSerialNumber(testExerciseTemplate.getSerialNumber());
                testExerciseInstance.setType(testExerciseTemplate.getType());
                testExerciseInstance.setCorrectAnswer(testExerciseTemplate.getAnswer());
                testExerciseInstance.setDisplayType(testExerciseTemplate.getDisplayType());
                this.testExerciseInstanceList.add(testExerciseInstance);
            }
        }
    }

    public TestPartInstancePublicVo(TestPartInstance testPartInstance) {
        this.setId(testPartInstance.getId());
        this.setCreateTime(testPartInstance.getCreateTime());
        this.setName(testPartInstance.getName());
        this.setUpdateTime(testPartInstance.getUpdateTime());
        this.setDescription(testPartInstance.getDescription());
        this.setScore(testPartInstance.getScore());
        this.setSerialNumber(testPartInstance.getSerialNumber());
        this.setType(testPartInstance.getType());
        this.setTestInstanceId(testPartInstance.getTestInstanceId());
        this.setScored(testPartInstance.getScored());
        this.setTestSubsectionInstanceId(testPartInstance.getTestSubsectionInstanceId());
    }

    public TestPartInstance voToEntity() {
        TestPartInstance testPartInstance = new TestPartInstance();
        if(this.getId() != null) {
            testPartInstance.setId(this.getId());
        }
        testPartInstance.setCreateTime(this.getCreateTime());
        testPartInstance.setName(this.getName());
        testPartInstance.setUpdateTime(this.getUpdateTime());
        testPartInstance.setDescription(this.getDescription());
        testPartInstance.setScore(this.getScore());
        testPartInstance.setSerialNumber(this.getSerialNumber());
        testPartInstance.setType(this.getType());
        testPartInstance.setTestInstanceId(this.getTestInstanceId());
        testPartInstance.setScored(this.getScored());
        testPartInstance.setTestSubsectionInstanceId(this.getTestSubsectionInstanceId());
        return testPartInstance;
    }

    public List getTestExerciseInstanceList() {
        return testExerciseInstanceList;
    }

    public void setTestExerciseInstanceList(List testExerciseInstanceList) {
        this.testExerciseInstanceList = testExerciseInstanceList;
    }
}
