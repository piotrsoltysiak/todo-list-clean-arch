package com.todoclean.adapters.inbound;

import org.assertj.core.api.AbstractThrowableAssert;

import lombok.NoArgsConstructor;


import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor
public class ErrorHandler {

    Exception thrown;

    AbstractThrowableAssert<?, ? extends Throwable> assertThrown(Class<?> type) {
        return assertThat(thrown)
                .isInstanceOf(type);
    }

    public void executeWithExceptionHandling(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            thrown = e;
        }
    }
}
