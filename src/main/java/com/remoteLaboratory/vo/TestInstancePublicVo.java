package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.TestInstance;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 测验实例公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "测验实例公用对象")
public class TestInstancePublicVo extends TestInstance {
    @ApiModelProperty(value = "测验大题实例列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestPartInstancePublicVo> testPartInstancePublicVoList;

    public TestInstancePublicVo() {

    }

    public TestInstancePublicVo(TestTemplatePublicVo testTemplatePublicVo) {
        this.setName(testTemplatePublicVo.getName());
        this.setChapterId(testTemplatePublicVo.getChapterId());
        this.setChapterName(testTemplatePublicVo.getChapterName());
        this.setChapterTitle(testTemplatePublicVo.getChapterTitle());
        this.setCourseId(testTemplatePublicVo.getCourseId());
        this.setCourseName(testTemplatePublicVo.getCourseName());
        this.setDescription(testTemplatePublicVo.getDescription());
        this.setScore(testTemplatePublicVo.getScore());
        this.setScored(0.0);
        this.setSectionId(testTemplatePublicVo.getSectionId());
        this.setSectionName(testTemplatePublicVo.getSectionName());
        this.setSectionTitle(testTemplatePublicVo.getSectionTitle());
        this.setStatus(0);
        this.setTestTemplateId(testTemplatePublicVo.getId());
        List<TestPartTemplatePublicVo> testPartTemplatePublicVoList = testTemplatePublicVo.getTestPartTemplatePublicVoList();
        if(CollectionUtils.isNotEmpty(testPartTemplatePublicVoList)) {
            this.testPartInstancePublicVoList = new ArrayList<>();
            for(TestPartTemplatePublicVo testPartTemplatePublicVo : testPartTemplatePublicVoList) {
                TestPartInstancePublicVo testPartInstancePublicVo = new TestPartInstancePublicVo(testPartTemplatePublicVo);
                this.testPartInstancePublicVoList.add(testPartInstancePublicVo);
            }
        }
    }

    public TestInstancePublicVo(TestInstance testInstance) {
        this.setId(testInstance.getId());
        this.setCreateTime(testInstance.getCreateTime());
        this.setName(testInstance.getName());
        this.setUpdateTime(testInstance.getUpdateTime());
        this.setChapterId(testInstance.getChapterId());
        this.setChapterName(testInstance.getChapterName());
        this.setChapterTitle(testInstance.getChapterTitle());
        this.setCourseId(testInstance.getCourseId());
        this.setCourseName(testInstance.getCourseName());
        this.setDescription(testInstance.getDescription());
        this.setScore(testInstance.getScore());
        this.setScored(testInstance.getScored());
        this.setUserId(testInstance.getUserId());
        this.setUserName(testInstance.getUserName());
        this.setTestExerciseInstanceId(testInstance.getTestExerciseInstanceId());
        this.setSectionId(testInstance.getSectionId());
        this.setSectionName(testInstance.getSectionName());
        this.setSectionTitle(testInstance.getSectionTitle());
        this.setStatus(testInstance.getStatus());
        this.setTestTemplateId(testInstance.getTestTemplateId());
    }

    public TestInstance voToEntity() {
        TestInstance testInstance = new TestInstance();
        if(this.getId() != null) {
            testInstance.setId(this.getId());
        }
        testInstance.setCreateTime(this.getCreateTime());
        testInstance.setName(this.getName());
        testInstance.setUpdateTime(this.getUpdateTime());
        testInstance.setChapterId(this.getChapterId());
        testInstance.setChapterName(this.getChapterName());
        testInstance.setChapterTitle(this.getChapterTitle());
        testInstance.setCourseId(this.getCourseId());
        testInstance.setCourseName(this.getCourseName());
        testInstance.setDescription(this.getDescription());
        testInstance.setScore(this.getScore());
        testInstance.setScored(this.getScored());
        testInstance.setUserId(this.getUserId());
        testInstance.setUserName(this.getUserName());
        testInstance.setTestExerciseInstanceId(this.getTestExerciseInstanceId());
        testInstance.setSectionId(this.getSectionId());
        testInstance.setSectionName(this.getSectionName());
        testInstance.setSectionTitle(this.getSectionTitle());
        testInstance.setStatus(this.getStatus());
        testInstance.setTestTemplateId(this.getTestTemplateId());
        return testInstance;
    }

    public List<TestPartInstancePublicVo> getTestPartInstancePublicVoList() {
        return testPartInstancePublicVoList;
    }

    public void setTestPartInstancePublicVoList(List<TestPartInstancePublicVo> testPartInstancePublicVoList) {
        this.testPartInstancePublicVoList = testPartInstancePublicVoList;
    }
}
