package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Integer>,JpaSpecificationExecutor<UploadFile> {

}
