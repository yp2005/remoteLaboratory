package com.remoteLaboratory.repositories;

import com.remoteLaboratory.entities.SysSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysSettingRepository extends JpaRepository<SysSetting, String>,JpaSpecificationExecutor<SysSetting> {
    SysSetting findByKeyName(@Param("keyName") String keyName);
}
