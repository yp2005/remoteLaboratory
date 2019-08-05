package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.DeviceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeviceOrderRepository extends JpaRepository<DeviceOrder, Integer>,JpaSpecificationExecutor<DeviceOrder> {
    DeviceOrder findFirstByYearAndMonthAndDay(@Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day);

    DeviceOrder findFirstByDeviceIdAndYearAndMonthAndDay(@Param("deviceId") Integer deviceId, @Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day);

    @Query("select d from DeviceOrder d where d.userId = :userId and d.endTime > :now")
    DeviceOrder findByUserIdAndTime(@Param("userId") Integer userId, @Param("now") Date now);
}
