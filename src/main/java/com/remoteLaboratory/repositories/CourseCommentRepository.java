package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment, Integer>,JpaSpecificationExecutor<CourseComment> {
    CourseComment findByCourseIdAndUserId(Integer courseId, Integer userId);
}
