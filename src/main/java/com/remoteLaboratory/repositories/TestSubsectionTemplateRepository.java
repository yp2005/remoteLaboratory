package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.TestSubsectionTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestSubsectionTemplateRepository extends JpaRepository<TestSubsectionTemplate, Integer>,JpaSpecificationExecutor<TestSubsectionTemplate> {
    void deleteByTestTemplateId(Integer testTemplateId);

    List<TestSubsectionTemplate> findByTestTemplateId(Integer testTemplateId);
}
