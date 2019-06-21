package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>,JpaSpecificationExecutor<Course> {

}
