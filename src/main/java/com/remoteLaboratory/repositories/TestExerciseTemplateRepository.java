package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.TestExerciseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestExerciseTemplateRepository extends JpaRepository<TestExerciseTemplate, Integer>,JpaSpecificationExecutor<TestExerciseTemplate> {
    void deleteByTestTemplateId(Integer testTemplateId);

    List<TestExerciseTemplate> findByTestPartTemplateId(Integer testPartTemplateId);

    List<TestExerciseTemplate> findByTestTemplateId(Integer testTemplateId);
}
