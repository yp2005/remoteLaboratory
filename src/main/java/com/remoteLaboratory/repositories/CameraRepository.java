package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Integer>,JpaSpecificationExecutor<Camera> {
    List<Camera> findBySrsServerId(@Param("srsServerId") Integer srsServerId);
}
