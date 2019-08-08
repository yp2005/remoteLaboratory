package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.CourseDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDeviceRepository extends JpaRepository<CourseDevice, Integer>,JpaSpecificationExecutor<CourseDevice> {
    CourseDevice findByCourseIdAndDeviceId(@Param("courseId") Integer courseId, @Param("deviceId") Integer deviceId);

    void deleteByCourseId(@Param("courseId") Integer courseId);
}
