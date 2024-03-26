package com.ivanm.flightadvisor.dao;

import com.ivanm.flightadvisor.dao.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<RouteEntity, Integer> {}
