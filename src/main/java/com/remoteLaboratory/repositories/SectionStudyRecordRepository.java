package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.SectionStudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionStudyRecordRepository extends JpaRepository<SectionStudyRecord, Integer>,JpaSpecificationExecutor<SectionStudyRecord> {
    SectionStudyRecord findBySectionIdAndUserId(@Param("sectionId") Integer sectionId, @Param("userId") Integer userId);

    List<SectionStudyRecord> findByChapterStudyRecordId(@Param("chapterStudyRecordId") Integer chapterStudyRecordId);

    @Query("update SectionStudyRecord csr set csr.chapterName = :chapterName, csr.chapterTitle = :chapterTitle where csr.chapterId = :chapterId")
    @Modifying
    void updateByChapterId(@Param("chapterId") Integer chapterId, @Param("chapterName") String chapterName, @Param("chapterTitle") String chapterTitle);


    @Query("update SectionStudyRecord csr set csr.sectionName = :sectionName, csr.sectionTitle = :sectionTitle where csr.sectionId = :sectionId")
    @Modifying
    void updateBySectionId(@Param("sectionId") Integer sectionId, @Param("sectionName") String sectionName, @Param("sectionTitle") String sectionTitle);
}
