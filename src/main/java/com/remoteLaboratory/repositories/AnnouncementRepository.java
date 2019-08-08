package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer>,JpaSpecificationExecutor<Announcement> {
    void deleteByCourseId(@Param("courseId") Integer courseId);
}
