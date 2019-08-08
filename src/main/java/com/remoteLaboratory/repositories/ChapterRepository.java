package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer>,JpaSpecificationExecutor<Chapter> {
    List<Chapter> findByCourseId(@Param("courseId") Integer courseId);

    void deleteByCourseId(@Param("courseId") Integer courseId);
}
