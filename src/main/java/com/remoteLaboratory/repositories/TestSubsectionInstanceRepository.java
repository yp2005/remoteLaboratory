package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.TestSubsectionInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestSubsectionInstanceRepository extends JpaRepository<TestSubsectionInstance, Integer>,JpaSpecificationExecutor<TestSubsectionInstance> {
    List<TestSubsectionInstance> findByTestInstanceId(@Param("testInstanceId") Integer testInstanceId);
}
