package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.TestExerciseInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestExerciseInstanceRepository extends JpaRepository<TestExerciseInstance, Integer>,JpaSpecificationExecutor<TestExerciseInstance> {
    List<TestExerciseInstance> findByTestPartInstanceId(@Param("testPartInstanceId") Integer testPartInstanceId);

    List<TestExerciseInstance> findByTestInstanceId(@Param("testInstanceId") Integer testInstanceId);
}
