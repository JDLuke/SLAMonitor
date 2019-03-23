package com.jdluke.sla_monitor.config;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SLAConfiguration {
    @Bean
    public Marker getSlaMarker() {
        return MarkerFactory.getMarker("SLA Monitor");
    }
}
