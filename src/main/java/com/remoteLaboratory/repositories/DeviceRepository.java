package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer>,JpaSpecificationExecutor<Device> {

}
