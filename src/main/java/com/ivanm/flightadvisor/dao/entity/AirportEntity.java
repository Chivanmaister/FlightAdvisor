package com.ivanm.flightadvisor.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_airport")
public class AirportEntity implements Serializable {

  @Serial private static final long serialVersionUID = 8337519143320978852L;

  @Id
  public Integer id;

  public String name;

  public String city;

  public String country;

  public String iata;

  public String icao;

  public Double latitude;

  public Double longitude;

  public Integer altitude;

  public String timezone;

  public String dst;

  public String databaseTimezone;

  public String type;

  public String source;

  @OneToMany(mappedBy = "sourceAirportId")
  public List<RouteEntity> sourceRouteEntities;

  @OneToMany(mappedBy = "destinationAirportId")
  public List<RouteEntity> destinationRouteEntities;

  public AirportEntity(Integer id, String name, String city, String country) {
    this.id = id;
    this.name = name;
    this.city = city;
    this.country = country;
  }
}
