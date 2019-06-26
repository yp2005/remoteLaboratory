package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.TestInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TestInstanceRepository extends JpaRepository<TestInstance, Integer>,JpaSpecificationExecutor<TestInstance> {
    TestInstance findByUserIdAndSectionId(@Param("userId") Integer userId, @Param("sectionId") Integer sectionId);

    TestInstance findByUserIdAndTestTemplateId(@Param("userId") Integer userId, @Param("testTemplateId") Integer testTemplateId);
}
