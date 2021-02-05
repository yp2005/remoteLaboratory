package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer>,JpaSpecificationExecutor<Exercise> {
    void deleteByCourseId(@Param("courseId") Integer courseId);
}
