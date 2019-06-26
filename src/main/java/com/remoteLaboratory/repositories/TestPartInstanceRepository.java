package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.TestPartInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestPartInstanceRepository extends JpaRepository<TestPartInstance, Integer>,JpaSpecificationExecutor<TestPartInstance> {
    List<TestPartInstance> findByTestInstanceId(@Param("testInstanceId") Integer testInstanceId);
}
