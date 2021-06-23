package com.todoclean.adapters.outbound.time;

import com.todoclean.application.ports.outbound.CurrentTimeSupplier;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TimeConfig {

    @Bean
    CurrentTimeSupplier currentTimeSupplier() {
        return LocalDateTime::now;
    }
}
