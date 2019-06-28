package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.SectionStudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionStudyRecordRepository extends JpaRepository<SectionStudyRecord, Integer>,JpaSpecificationExecutor<SectionStudyRecord> {
    SectionStudyRecord findBySectionIdAndUserId(@Param("sectionId") Integer sectionId, @Param("userId") Integer userId);

    List<SectionStudyRecord> findByChapterStudyRecordId(@Param("chapterStudyRecordId") Integer chapterStudyRecordId);
}
