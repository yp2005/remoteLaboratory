package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.Camera;
import com.remoteLaboratory.entities.TestTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 测验模板公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "测验模板公用对象")
public class TestTemplatePublicVo extends TestTemplate {
    @ApiModelProperty(value = "测验大题模板列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestPartTemplatePublicVo> testPartTemplatePublicVoList;

    public TestTemplatePublicVo() {

    }

    public TestTemplatePublicVo(TestTemplate testTemplate) {
        this.setId(testTemplate.getId());
        this.setCreateTime(testTemplate.getCreateTime());
        this.setName(testTemplate.getName());
        this.setUpdateTime(testTemplate.getUpdateTime());
        this.setChapterId(testTemplate.getChapterId());
        this.setChapterName(testTemplate.getChapterName());
        this.setChapterTitle(testTemplate.getChapterTitle());
        this.setCourseId(testTemplate.getCourseId());
        this.setCourseName(testTemplate.getCourseName());
        this.setDescription(testTemplate.getDescription());
        this.setScore(testTemplate.getScore());
        this.setSectionId(testTemplate.getSectionId());
        this.setSectionName(testTemplate.getSectionName());
        this.setSectionTitle(testTemplate.getSectionTitle());
    }

    public TestTemplate voToEntity() {
        TestTemplate testTemplate = new TestTemplate();
        if(this.getId() != null) {
            testTemplate.setId(this.getId());
        }
        testTemplate.setCreateTime(this.getCreateTime());
        testTemplate.setName(this.getName());
        testTemplate.setUpdateTime(this.getUpdateTime());
        testTemplate.setChapterId(this.getChapterId());
        testTemplate.setChapterName(this.getChapterName());
        testTemplate.setChapterTitle(this.getChapterTitle());
        testTemplate.setCourseId(this.getCourseId());
        testTemplate.setCourseName(this.getCourseName());
        testTemplate.setDescription(this.getDescription());
        testTemplate.setScore(this.getScore());
        testTemplate.setSectionId(this.getSectionId());
        testTemplate.setSectionName(this.getSectionName());
        testTemplate.setSectionTitle(this.getSectionTitle());
        return testTemplate;
    }

    public List<TestPartTemplatePublicVo> getTestPartTemplatePublicVoList() {
        return testPartTemplatePublicVoList;
    }

    public void setTestPartTemplatePublicVoList(List<TestPartTemplatePublicVo> testPartTemplatePublicVoList) {
        this.testPartTemplatePublicVoList = testPartTemplatePublicVoList;
    }
}
