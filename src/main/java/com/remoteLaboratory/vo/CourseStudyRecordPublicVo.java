package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.CourseStudyRecord;
import com.remoteLaboratory.entities.TestRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 课程学习记录公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "课程学习记录公用对象")
public class CourseStudyRecordPublicVo extends CourseStudyRecord {
    @ApiModelProperty(value = "章学习记录列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ChapterStudyRecordPublicVo> chapterStudyRecordPublicVoList;

    @ApiModelProperty(value = "实验报告记录列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestRecord> testRecordList;

    public CourseStudyRecordPublicVo() {

    }

    public CourseStudyRecordPublicVo(CourseStudyRecord courseStudyRecord) {
        this.setId(courseStudyRecord.getId());
        this.setCreateTime(courseStudyRecord.getCreateTime());
        this.setUpdateTime(courseStudyRecord.getUpdateTime());
        this.setChapterId(courseStudyRecord.getChapterId());
        this.setChapterName(courseStudyRecord.getChapterName());
        this.setChapterTitle(courseStudyRecord.getChapterTitle());
        this.setStudied(courseStudyRecord.getStudied());
        this.setUserId(courseStudyRecord.getUserId());
        this.setUserName(courseStudyRecord.getUserName());
        this.setCourseId(courseStudyRecord.getCourseId());
        this.setCourseName(courseStudyRecord.getCourseName());
        this.setSectionId(courseStudyRecord.getSectionId());
        this.setSectionName(courseStudyRecord.getSectionName());
        this.setSectionTitle(courseStudyRecord.getSectionTitle());
        this.setCourseMainImg(courseStudyRecord.getCourseMainImg());
        this.setCourseIntroduction(courseStudyRecord.getCourseIntroduction());
        this.setStatus(courseStudyRecord.getStatus());
        this.setIsQuestionnaireFinish(courseStudyRecord.getIsQuestionnaireFinish());
        this.setQuestionnaireTemplateId(courseStudyRecord.getQuestionnaireTemplateId());
        this.setCourseVideoDesc(courseStudyRecord.getCourseVideoDesc());
        this.setTestTemplateNumber(courseStudyRecord.getTestTemplateNumber());
        this.setTestTemplateFinishedNumber(courseStudyRecord.getTestTemplateFinishedNumber());
        this.setTestTemplateSubmitedNumber(courseStudyRecord.getTestTemplateSubmitedNumber());
        this.setScore(courseStudyRecord.getScore());
        this.setPreStudyScore(courseStudyRecord.getPreStudyScore());
        this.setOperationScore(courseStudyRecord.getOperationScore());
        this.setDataScore(courseStudyRecord.getDataScore());
        this.setDataAnalysisScore(courseStudyRecord.getDataAnalysisScore());
        this.setReportScore(courseStudyRecord.getReportScore());
        this.setGraded(courseStudyRecord.getGraded());
        this.setIsOld(courseStudyRecord.getIsOld());
        this.setClass1(courseStudyRecord.getClass1());
        this.setGraded(courseStudyRecord.getGraded());
    }

    public CourseStudyRecord voToEntity() {
        CourseStudyRecord courseStudyRecord = new CourseStudyRecord();
        if(this.getId() != null) {
            courseStudyRecord.setId(this.getId());
        }
        courseStudyRecord.setCreateTime(this.getCreateTime());
        courseStudyRecord.setUpdateTime(this.getUpdateTime());
        courseStudyRecord.setChapterId(this.getChapterId());
        courseStudyRecord.setChapterName(this.getChapterName());
        courseStudyRecord.setChapterTitle(this.getChapterTitle());
        courseStudyRecord.setStudied(this.getStudied());
        courseStudyRecord.setUserId(this.getUserId());
        courseStudyRecord.setUserName(this.getUserName());
        courseStudyRecord.setCourseId(this.getCourseId());
        courseStudyRecord.setCourseName(this.getCourseName());
        courseStudyRecord.setSectionId(this.getSectionId());
        courseStudyRecord.setSectionName(this.getSectionName());
        courseStudyRecord.setSectionTitle(this.getSectionTitle());
        courseStudyRecord.setCourseMainImg(this.getCourseMainImg());
        courseStudyRecord.setCourseIntroduction(this.getCourseIntroduction());
        courseStudyRecord.setCourseVideoDesc(this.getCourseVideoDesc());
        courseStudyRecord.setStatus(this.getStatus());
        courseStudyRecord.setIsQuestionnaireFinish(this.getIsQuestionnaireFinish());
        courseStudyRecord.setQuestionnaireTemplateId(this.getQuestionnaireTemplateId());
        courseStudyRecord.setTestTemplateNumber(this.getTestTemplateNumber());
        courseStudyRecord.setTestTemplateFinishedNumber(this.getTestTemplateFinishedNumber());
        courseStudyRecord.setTestTemplateSubmitedNumber(this.getTestTemplateSubmitedNumber());
        courseStudyRecord.setScore(this.getScore());
        courseStudyRecord.setPreStudyScore(this.getPreStudyScore());
        courseStudyRecord.setOperationScore(this.getOperationScore());
        courseStudyRecord.setDataScore(this.getDataScore());
        courseStudyRecord.setDataAnalysisScore(this.getDataAnalysisScore());
        courseStudyRecord.setReportScore(this.getReportScore());
        courseStudyRecord.setGraded(this.getGraded());
        courseStudyRecord.setIsOld(this.getIsOld());
        courseStudyRecord.setClass1(this.getClass1());
        courseStudyRecord.setGraded(this.getGraded());
        return courseStudyRecord;
    }

    public List<ChapterStudyRecordPublicVo> getChapterStudyRecordPublicVoList() {
        return chapterStudyRecordPublicVoList;
    }

    public void setChapterStudyRecordPublicVoList(List<ChapterStudyRecordPublicVo> chapterStudyRecordPublicVoList) {
        this.chapterStudyRecordPublicVoList = chapterStudyRecordPublicVoList;
    }

    public List<TestRecord> getTestRecordList() {
        return testRecordList;
    }

    public void setTestRecordList(List<TestRecord> testRecordList) {
        this.testRecordList = testRecordList;
    }
}
