package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.TestInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestInstanceRepository extends JpaRepository<TestInstance, Integer>,JpaSpecificationExecutor<TestInstance> {
    TestInstance findByUserIdAndTestTemplateId(@Param("userId") Integer userId, @Param("testTemplateId") Integer testTemplateId);

    List<TestInstance> findByUserId(@Param("userId") Integer userId);

    List<TestInstance> findByUserIdAndCourseId(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    @Query("update TestInstance set isOld = true where courseId = :courseId and isOld = false")
    @Modifying
    void updateOldByCourseId(@Param("courseId") Integer courseId);
}
