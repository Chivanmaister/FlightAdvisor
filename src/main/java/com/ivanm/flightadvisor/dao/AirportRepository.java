package com.ivanm.flightadvisor.dao;

import com.ivanm.flightadvisor.dao.entity.AirportEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AirportRepository extends JpaRepository<AirportEntity, Integer> {

  @Query(
      """
      SELECT new com.ivanm.flightadvisor.dao.entity.AirportEntity(a.id, a.name, a.city, a.country)
      FROM AirportEntity a
      WHERE a.city LIKE %?1%
      """)
  List<AirportEntity> findAirportByCityName(String name);
}
