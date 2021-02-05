package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.TestTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestTemplateRepository extends JpaRepository<TestTemplate, Integer>,JpaSpecificationExecutor<TestTemplate> {
    List<TestTemplate> findByCourseIdAndTestType(@Param("courseId") Integer courseId, @Param("testType") Integer testType);

    @Query("select t from TestTemplate t where t.testType = 2 and t.courseId = :courseId")
    TestTemplate findQuestionnaireByCourseId(@Param("courseId") Integer courseId);
}
