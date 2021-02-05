package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User> {
    User findByUserName(String userName);

    User findByUserKey(String userKey);
}
