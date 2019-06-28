package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer>,JpaSpecificationExecutor<Section> {
    List<Section> findByChapterId(@Param("chapterId") Integer chapterId);
}
