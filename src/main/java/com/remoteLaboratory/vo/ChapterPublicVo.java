package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.Chapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 章公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "章公用对象")
public class ChapterPublicVo extends Chapter {
    @ApiModelProperty(value = "节列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SectionOutput> sectionList;

    public ChapterPublicVo() {

    }

    public ChapterPublicVo(Chapter chapter) {
        this.setId(chapter.getId());
        this.setCreateTime(chapter.getCreateTime());
        this.setUpdateTime(chapter.getUpdateTime());
        this.setName(chapter.getName());
        this.setCourseId(chapter.getCourseId());
        this.setCourseName(chapter.getCourseName());
        this.setTitle(chapter.getTitle());
    }

    public Chapter voToEntity() {
        Chapter chapter = new Chapter();
        if(this.getId() != null) {
            chapter.setId(this.getId());
        }
        chapter.setCreateTime(this.getCreateTime());
        chapter.setUpdateTime(this.getUpdateTime());
        chapter.setName(this.getName());
        chapter.setCourseId(this.getCourseId());
        chapter.setCourseName(this.getCourseName());
        chapter.setTitle(this.getTitle());
        return chapter;
    }

    public List<SectionOutput> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<SectionOutput> sectionList) {
        this.sectionList = sectionList;
    }
}
