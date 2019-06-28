package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.ChapterStudyRecord;
import com.remoteLaboratory.entities.SectionStudyRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 章学习记录公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "章学习记录公用对象")
public class ChapterStudyRecordPublicVo extends ChapterStudyRecord {
    @ApiModelProperty(value = "小节学习记录列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SectionStudyRecord> sectionStudyRecordList;

    public ChapterStudyRecordPublicVo() {

    }

    public ChapterStudyRecordPublicVo(ChapterStudyRecord chapterStudyRecord) {
        this.setId(chapterStudyRecord.getId());
        this.setCreateTime(chapterStudyRecord.getCreateTime());
        this.setUpdateTime(chapterStudyRecord.getUpdateTime());
        this.setChapterId(chapterStudyRecord.getChapterId());
        this.setChapterName(chapterStudyRecord.getChapterName());
        this.setChapterTitle(chapterStudyRecord.getChapterTitle());
        this.setCourseStudyRecordId(chapterStudyRecord.getCourseStudyRecordId());
        this.setStudied(chapterStudyRecord.getStudied());
        this.setUserId(chapterStudyRecord.getUserId());
        this.setUserName(chapterStudyRecord.getUserName());
    }

    public List<SectionStudyRecord> getSectionStudyRecordList() {
        return sectionStudyRecordList;
    }

    public void setSectionStudyRecordList(List<SectionStudyRecord> sectionStudyRecordList) {
        this.sectionStudyRecordList = sectionStudyRecordList;
    }

    public ChapterStudyRecord voToEntity() {
        ChapterStudyRecord chapterStudyRecord = new ChapterStudyRecord();
        if(this.getId() != null) {
            chapterStudyRecord.setId(this.getId());
        }
        chapterStudyRecord.setCreateTime(this.getCreateTime());
        chapterStudyRecord.setUpdateTime(this.getUpdateTime());
        chapterStudyRecord.setChapterId(this.getChapterId());
        chapterStudyRecord.setChapterName(this.getChapterName());
        chapterStudyRecord.setChapterTitle(this.getChapterTitle());
        chapterStudyRecord.setCourseStudyRecordId(this.getCourseStudyRecordId());
        chapterStudyRecord.setStudied(this.getStudied());
        chapterStudyRecord.setUserId(this.getUserId());
        chapterStudyRecord.setUserName(this.getUserName());
        return chapterStudyRecord;
    }
}
