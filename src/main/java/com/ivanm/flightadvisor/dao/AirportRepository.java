package com.ivanm.flightadvisor.dao;

import com.ivanm.flightadvisor.dao.entity.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<AirportEntity, Integer> {}
