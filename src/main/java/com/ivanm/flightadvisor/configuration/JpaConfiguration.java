package com.ivanm.flightadvisor.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan("com.ivanm.flightadvisor.dao.entity")
@EnableJpaRepositories("com.ivanm.flightadvisor.dao")
public class JpaConfiguration {}
