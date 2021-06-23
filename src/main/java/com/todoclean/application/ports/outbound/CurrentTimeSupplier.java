package com.todoclean.application.ports.outbound;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public interface CurrentTimeSupplier extends Supplier<LocalDateTime> {

}
