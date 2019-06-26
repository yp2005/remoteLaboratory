package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.TestPartTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestPartTemplateRepository extends JpaRepository<TestPartTemplate, Integer>,JpaSpecificationExecutor<TestPartTemplate> {
    void deleteByTestTemplateId(@Param("testTemplateId") Integer testTemplateId);

    List<TestPartTemplate> findByTestTemplateId(@Param("testTemplateId") Integer testTemplateId);
}
