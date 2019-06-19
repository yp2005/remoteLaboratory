package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.LogRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRecordRepository extends JpaRepository<LogRecord, Integer>,JpaSpecificationExecutor<LogRecord> {

}
