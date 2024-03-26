package com.ivanm.flightadvisor.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "t_route")
public class RouteEntity implements Serializable {

  @Serial private static final long serialVersionUID = 7771904267998893651L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer id;

  public String airline;

  public String airlineId;

  public String sourceAirport;

  public String sourceAirportId;

  public String destinationAirport;

  public String destinationAirportId;

  public String codeShare;

  public String stops;

  public String equipment;

  public String price;
}
