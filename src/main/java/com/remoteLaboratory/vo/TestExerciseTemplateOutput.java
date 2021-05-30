package com.remoteLaboratory.vo;

import com.remoteLaboratory.entities.TestExerciseTemplate;
import io.swagger.annotations.ApiModel;

/**
 * 测试小题模板输出
 *
 * @Author: yupeng
 */
@ApiModel(value = "测试小题模板输出")
public class TestExerciseTemplateOutput extends TestExerciseTemplate {

    public TestExerciseTemplateOutput(TestExerciseTemplate testExerciseTemplate) {
        this.setId(testExerciseTemplate.getId());
        this.setUpdateTime(testExerciseTemplate.getUpdateTime());
        this.setCreateTime(testExerciseTemplate.getCreateTime());
        this.setAnswer(testExerciseTemplate.getAnswer());
        this.setDisplayType(testExerciseTemplate.getDisplayType());
        this.setExerciseContent(testExerciseTemplate.getExerciseContent());
        this.setExerciseId(testExerciseTemplate.getExerciseId());
        this.setExercisesType(testExerciseTemplate.getExercisesType());
        this.setOptions(testExerciseTemplate.getOptions());
        this.setScore(testExerciseTemplate.getScore());
        this.setSerialNumber(testExerciseTemplate.getSerialNumber());
        this.setTestPartTemplateId(testExerciseTemplate.getTestPartTemplateId());
        this.setTestSubsectionTemplateId(testExerciseTemplate.getTestSubsectionTemplateId());
        this.setTestTemplateId(testExerciseTemplate.getTestTemplateId());
        this.setType(testExerciseTemplate.getType());
    }
}
