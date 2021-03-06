package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.CourseStudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseStudyRecordRepository extends JpaRepository<CourseStudyRecord, Integer>,JpaSpecificationExecutor<CourseStudyRecord> {
    CourseStudyRecord findByCourseIdAndUserId(@Param("courseId") Integer courseId, @Param("userId") Integer userId);

    List<CourseStudyRecord> findByCourseIdAndStatus(@Param("courseId") Integer courseId, @Param("status") Integer status);

    @Query("update CourseStudyRecord csr set csr.courseName = :courseName, csr.courseMainImg = :courseMainImg, csr.courseIntroduction = :courseIntroduction where csr.courseId = :courseId")
    @Modifying
    void updateByCourseId(@Param("courseId") Integer courseId, @Param("courseName") String courseName, @Param("courseMainImg") String courseMainImg, @Param("courseIntroduction") String courseIntroduction);

    @Query("update CourseStudyRecord csr set csr.chapterName = :chapterName, csr.chapterTitle = :chapterTitle where csr.chapterId = :chapterId")
    @Modifying
    void updateByChapterId(@Param("chapterId") Integer chapterId, @Param("chapterName") String chapterName, @Param("chapterTitle") String chapterTitle);

    @Query("update CourseStudyRecord csr set csr.sectionName = :sectionName, csr.sectionTitle = :sectionTitle where csr.sectionId = :sectionId")
    @Modifying
    void updateBySectionId(@Param("sectionId") Integer sectionId, @Param("sectionName") String sectionName, @Param("sectionTitle") String sectionTitle);

    @Query("update CourseStudyRecord csr set csr.isOld = true where csr.courseId = :courseId and csr.isOld = false")
    @Modifying
    void updateOldByCourseId(@Param("courseId") Integer courseId);

    @Query("select distinct class1 from CourseStudyRecord where courseId = :courseId")
    List<String> findClassByCourseId(@Param("courseId") Integer courseId);

    @Query("select distinct grade from CourseStudyRecord where courseId = :courseId")
    List<String> findGradeByCourseId(@Param("courseId") Integer courseId);

    @Query("select distinct class1 from CourseStudyRecord where courseId = :courseId and isOld = :isOld")
    List<String> findClassByCourseIdAndIsOld(@Param("courseId") Integer courseId, @Param("isOld") Boolean isOld);

    @Query("select distinct grade from CourseStudyRecord where courseId = :courseId and isOld = :isOld")
    List<String> findGradeByCourseIdAndIsOld(@Param("courseId") Integer courseId, @Param("isOld") Boolean isOld);

}
