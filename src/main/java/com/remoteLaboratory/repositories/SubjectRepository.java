package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer>,JpaSpecificationExecutor<Subject> {

}
