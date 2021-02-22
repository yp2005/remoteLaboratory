package com.remoteLaboratory.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remoteLaboratory.entities.Course;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 课程公用对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "课程公用对象")
public class CoursePublicVo extends Course {
    @ApiModelProperty(value = "章列表")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ChapterPublicVo> chapterList;

    public CoursePublicVo() {

    }

    public CoursePublicVo(Course course) {
        this.setId(course.getId());
        this.setCreateTime(course.getCreateTime());
        this.setUpdateTime(course.getUpdateTime());
        this.setDescription(course.getDescription());
        this.setIntroduction(course.getIntroduction());
        this.setLogo(course.getLogo());
        this.setMainImg(course.getMainImg());
        this.setName(course.getName());
        this.setStandard(course.getStandard());
        this.setStatus(course.getStatus());
        this.setStudentNumber(course.getStudentNumber());
        this.setSubjectNumber(course.getSubjectNumber());
        this.setTeacherId(course.getTeacherId());
        this.setTeacherName(course.getTeacherName());
        this.setVideoDesc(course.getVideoDesc());
        this.setCommentNumber(course.getCommentNumber());
        this.setExperimentStarted(course.getExperimentStarted());
        this.setPreStudyScore(course.getPreStudyScore());
        this.setReportScore(course.getReportScore());
    }

    public Course voToEntity() {
        Course course = new Course();
        if(this.getId() != null) {
            course.setId(this.getId());
        }
        course.setCreateTime(this.getCreateTime());
        course.setUpdateTime(this.getUpdateTime());
        course.setDescription(this.getDescription());
        course.setIntroduction(this.getIntroduction());
        course.setLogo(this.getLogo());
        course.setMainImg(this.getMainImg());
        course.setName(this.getName());
        course.setStandard(this.getStandard());
        course.setStatus(this.getStatus());
        course.setStudentNumber(this.getStudentNumber());
        course.setSubjectNumber(this.getSubjectNumber());
        course.setTeacherId(this.getTeacherId());
        course.setTeacherName(this.getTeacherName());
        course.setVideoDesc(this.getVideoDesc());
        course.setCommentNumber(this.getCommentNumber());
        course.setExperimentStarted(this.getExperimentStarted());
        course.setPreStudyScore(this.getPreStudyScore());
        course.setReportScore(this.getReportScore());
        return course;
    }

    public List<ChapterPublicVo> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<ChapterPublicVo> chapterList) {
        this.chapterList = chapterList;
    }
}
