package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment, Integer>,JpaSpecificationExecutor<CourseComment> {
    CourseComment findByCourseIdAndUserId(Integer courseId, Integer userId);

    @Query("update CourseComment set mainPageDisplay = false where courseId = :courseId and mainPageDisplay = true")
    @Modifying
    void cancelMainPageDisplayByCourseId(@Param("courseId") Integer courseId);
}
