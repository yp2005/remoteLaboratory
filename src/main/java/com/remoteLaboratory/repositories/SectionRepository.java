package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer>,JpaSpecificationExecutor<Section> {

}
