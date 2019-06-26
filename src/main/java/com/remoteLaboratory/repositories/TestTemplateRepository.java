package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.TestTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTemplateRepository extends JpaRepository<TestTemplate, Integer>,JpaSpecificationExecutor<TestTemplate> {
    TestTemplate findBySectionId(@Param("sectionId") Integer sectionId);
}
