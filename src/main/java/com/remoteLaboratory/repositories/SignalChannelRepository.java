package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.SignalChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignalChannelRepository extends JpaRepository<SignalChannel, Integer>,JpaSpecificationExecutor<SignalChannel> {
    List<SignalChannel> findByDeviceId(@Param("deviceId") Integer deviceId);
}
