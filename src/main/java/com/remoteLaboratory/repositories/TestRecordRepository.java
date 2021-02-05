package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRecordRepository extends JpaRepository<TestRecord, Integer>,JpaSpecificationExecutor<TestRecord> {
    List<TestRecord> findByCourseStudyRecordId(Integer courseStudyRecordId);

    TestRecord findByTestTemplateIdAndUserId(Integer testTemplateId, Integer userId);
}
