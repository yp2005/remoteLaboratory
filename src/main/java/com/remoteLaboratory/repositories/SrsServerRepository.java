package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.SrsServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SrsServerRepository extends JpaRepository<SrsServer, Integer>,JpaSpecificationExecutor<SrsServer> {
    SrsServer findByUniqueKey(@Param("uniqueKey") String uniqueKey);
}
