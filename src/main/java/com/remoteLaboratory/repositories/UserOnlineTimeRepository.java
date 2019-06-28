package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.UserOnlineTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOnlineTimeRepository extends JpaRepository<UserOnlineTime, Integer>,JpaSpecificationExecutor<UserOnlineTime> {

}
