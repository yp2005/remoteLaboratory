package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Integer>,JpaSpecificationExecutor<Simulation> {

}
