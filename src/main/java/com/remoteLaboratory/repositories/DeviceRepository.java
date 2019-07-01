package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer>,JpaSpecificationExecutor<Device> {
    @Query("select d from Device d where d.type = 1")
    List<Device> findOnlineDevice();
}
