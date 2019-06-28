package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.ChapterStudyRecord;
import com.remoteLaboratory.entities.CourseStudyRecord;
import com.remoteLaboratory.entities.SectionStudyRecord;
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
        return courseStudyRecord;
    }

    public List<ChapterStudyRecordPublicVo> getChapterStudyRecordPublicVoList() {
        return chapterStudyRecordPublicVoList;
    }

    public void setChapterStudyRecordPublicVoList(List<ChapterStudyRecordPublicVo> chapterStudyRecordPublicVoList) {
        this.chapterStudyRecordPublicVoList = chapterStudyRecordPublicVoList;
    }
}
