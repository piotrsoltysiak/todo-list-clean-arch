package com.todoclean.adapters.outbound.time;

import com.todoclean.application.ports.outbound.CurrentTimeSupplier;
import java.time.LocalDateTime;
import java.util.Optional;

public class ConfigurableClock implements CurrentTimeSupplier {
    private LocalDateTime configuredTime;

    public void setTime(LocalDateTime time) {
        this.configuredTime = time;
    }

    @Override
    public LocalDateTime get() {
        return Optional.ofNullable(configuredTime).orElseGet(LocalDateTime::now);
    }
}
