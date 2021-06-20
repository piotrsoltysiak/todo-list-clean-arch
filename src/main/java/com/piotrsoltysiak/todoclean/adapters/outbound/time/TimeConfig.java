package com.piotrsoltysiak.todoclean.adapters.outbound.time;

import com.piotrsoltysiak.todoclean.application.ports.outbound.CurrentTimeSupplier;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

    @Bean
    CurrentTimeSupplier currentTimeSupplier() {
        return LocalDateTime::now;
    }
}
