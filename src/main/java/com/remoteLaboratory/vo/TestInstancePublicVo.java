package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.TestExerciseInstance;
import com.remoteLaboratory.entities.TestExerciseTemplate;
import com.remoteLaboratory.entities.TestInstance;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 实验报告实例公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "实验报告实例公用对象")
public class TestInstancePublicVo extends TestInstance {
    @ApiModelProperty(value = "分项实例列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestSubsectionInstancePublicVo> testSubsectionInstancePublicVoList;

    @ApiModelProperty(value = "实验报告小题实例列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestExerciseInstance> testExerciseInstanceList;

    public TestInstancePublicVo() {

    }

    public TestInstancePublicVo(TestTemplatePublicVo testTemplatePublicVo) {
        this.setName(testTemplatePublicVo.getName());
        this.setCourseId(testTemplatePublicVo.getCourseId());
        this.setCourseName(testTemplatePublicVo.getCourseName());
        this.setDescription(testTemplatePublicVo.getDescription());
        this.setScore(testTemplatePublicVo.getScore());
        this.setScored(0.0);
        this.setTestType(testTemplatePublicVo.getTestType());
        this.setStatus(0);
        this.setIsOld(false);
        this.setTestTemplateId(testTemplatePublicVo.getId());
        if(testTemplatePublicVo.getTestType().equals(1)) { // 实验报告
            List<TestSubsectionTemplatePublicVo> testSubsectionTemplatePublicVoList = testTemplatePublicVo.getTestSubsectionTemplatePublicVoList();
            if(CollectionUtils.isNotEmpty(testSubsectionTemplatePublicVoList)) {
                this.testSubsectionInstancePublicVoList = new ArrayList<>();
                testSubsectionTemplatePublicVoList.forEach(testSubsectionTemplatePublicVo -> {
                    TestSubsectionInstancePublicVo testSubsectionInstancePublicVo = new TestSubsectionInstancePublicVo(testSubsectionTemplatePublicVo);
                    this.testSubsectionInstancePublicVoList.add(testSubsectionInstancePublicVo);
                });
            }
        }
        else { // 问卷调查
            List<TestExerciseTemplate> testExerciseTemplateList = testTemplatePublicVo.getTestExerciseTemplateList();
            if(CollectionUtils.isNotEmpty(testExerciseTemplateList)) {
                this.testExerciseInstanceList = new ArrayList<>();
                testExerciseTemplateList.forEach(testExerciseTemplate -> {
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
                });
            }
        }

    }

    public TestInstancePublicVo(TestInstance testInstance) {
        this.setId(testInstance.getId());
        this.setCreateTime(testInstance.getCreateTime());
        this.setName(testInstance.getName());
        this.setUpdateTime(testInstance.getUpdateTime());
        this.setCourseId(testInstance.getCourseId());
        this.setCourseName(testInstance.getCourseName());
        this.setDescription(testInstance.getDescription());
        this.setScore(testInstance.getScore());
        this.setScored(testInstance.getScored());
        this.setUserId(testInstance.getUserId());
        this.setUserName(testInstance.getUserName());
        this.setClass1(testInstance.getClass1());
        this.setGrade(testInstance.getGrade());
        this.setTestExerciseInstanceId(testInstance.getTestExerciseInstanceId());
        this.setTestType(testInstance.getTestType());
        this.setStatus(testInstance.getStatus());
        this.setIsOld(testInstance.getIsOld());
        this.setTestTemplateId(testInstance.getTestTemplateId());
        this.setSubmitTime(testInstance.getSubmitTime());
        this.setUserKey(testInstance.getUserKey());
    }

    public TestInstance voToEntity() {
        TestInstance testInstance = new TestInstance();
        if(this.getId() != null) {
            testInstance.setId(this.getId());
        }
        testInstance.setCreateTime(this.getCreateTime());
        testInstance.setName(this.getName());
        testInstance.setUpdateTime(this.getUpdateTime());
        testInstance.setCourseId(this.getCourseId());
        testInstance.setCourseName(this.getCourseName());
        testInstance.setDescription(this.getDescription());
        testInstance.setScore(this.getScore());
        testInstance.setScored(this.getScored());
        testInstance.setUserId(this.getUserId());
        testInstance.setUserName(this.getUserName());
        testInstance.setClass1(this.getClass1());
        testInstance.setGrade(this.getGrade());
        testInstance.setTestExerciseInstanceId(this.getTestExerciseInstanceId());
        testInstance.setTestType(this.getTestType());
        testInstance.setStatus(this.getStatus());
        testInstance.setIsOld(this.getIsOld());
        testInstance.setTestTemplateId(this.getTestTemplateId());
        testInstance.setSubmitTime(this.getSubmitTime());
        testInstance.setUserKey(this.getUserKey());
        return testInstance;
    }

    public List<TestSubsectionInstancePublicVo> getTestSubsectionInstancePublicVoList() {
        return testSubsectionInstancePublicVoList;
    }

    public void setTestSubsectionInstancePublicVoList(List<TestSubsectionInstancePublicVo> testSubsectionInstancePublicVoList) {
        this.testSubsectionInstancePublicVoList = testSubsectionInstancePublicVoList;
    }

    public List<TestExerciseInstance> getTestExerciseInstanceList() {
        return testExerciseInstanceList;
    }

    public void setTestExerciseInstanceList(List<TestExerciseInstance> testExerciseInstanceList) {
        this.testExerciseInstanceList = testExerciseInstanceList;
    }
}
