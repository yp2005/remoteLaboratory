package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.LogRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface LogRecordRepository extends JpaRepository<LogRecord, Integer>,JpaSpecificationExecutor<LogRecord> {
    @Query("delete from LogRecord where createTime < :logRetainTime")
    @Modifying
    void deleteByTime(@Param("logRetainTime") Date logRetainTime);
}
