package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.CourseStudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseStudyRecordRepository extends JpaRepository<CourseStudyRecord, Integer>,JpaSpecificationExecutor<CourseStudyRecord> {
    CourseStudyRecord findByCourseIdAndUserId(@Param("courseId") Integer courseId, @Param("userId") Integer userId);

    List<CourseStudyRecord> findByCourseIdAndStatus(@Param("courseId") Integer courseId, @Param("status") Integer status);
}
