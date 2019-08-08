package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.ChapterStudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterStudyRecordRepository extends JpaRepository<ChapterStudyRecord, Integer>,JpaSpecificationExecutor<ChapterStudyRecord> {
    List<ChapterStudyRecord> findByCourseStudyRecordId(@Param("courseStudyRecordId") Integer courseStudyRecordId);

    ChapterStudyRecord findByCourseStudyRecordIdAndChapterId(@Param("courseStudyRecordId") Integer courseStudyRecordId, @Param("chapterId") Integer chapterId);

    @Query("update ChapterStudyRecord csr set csr.chapterName = :chapterName, csr.chapterTitle = :chapterTitle where csr.chapterId = :chapterId")
    @Modifying
    void updateByChapterId(@Param("chapterId") Integer chapterId, @Param("chapterName") String chapterName, @Param("chapterTitle") String chapterTitle);
}
