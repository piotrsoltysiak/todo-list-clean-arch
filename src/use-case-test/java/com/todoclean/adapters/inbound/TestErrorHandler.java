package com.todoclean.adapters.inbound;

import org.assertj.core.api.AbstractThrowableAssert;

import lombok.NoArgsConstructor;


import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor
public class TestErrorHandler {

    Exception thrown;

    AbstractThrowableAssert<?, ? extends Throwable> assertThrown(Class<?> type) {
        return assertThat(thrown)
                .isInstanceOf(type);
    }

    void executeWithExceptionHandling(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            if (thrown == null) {
                thrown = e;
            } else {
                throw new IllegalStateException("Multiple exceptions thrown during scenario!", e);
            }
        }
    }
}
