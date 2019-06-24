package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer>,JpaSpecificationExecutor<Chapter> {

}
