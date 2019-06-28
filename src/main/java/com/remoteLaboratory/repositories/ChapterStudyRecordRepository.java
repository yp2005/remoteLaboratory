package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.ChapterStudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterStudyRecordRepository extends JpaRepository<ChapterStudyRecord, Integer>,JpaSpecificationExecutor<ChapterStudyRecord> {
    List<ChapterStudyRecord> findByCourseStudyRecordId(@Param("courseStudyRecordId") Integer courseStudyRecordId);
}
