package com.todoclean.application;

import com.todoclean.adapters.outbound.persistence.inmemory.InMemoryTodoListRepository;
import com.todoclean.adapters.outbound.time.ConfigurableClock;
import com.todoclean.application.ports.outbound.TodoListRepository;
import java.time.LocalDateTime;

public class TestTodoListFacade extends TodoListFacade {
    private final ConfigurableClock configurableClock;

    public TestTodoListFacade() {
        this(
                new InMemoryTodoListRepository(),
                new ConfigurableClock()
        );
    }

    public TestTodoListFacade(TodoListRepository todoListRepository,
                              ConfigurableClock configurableClock) {
        super(todoListRepository, configurableClock);
        this.configurableClock = configurableClock;
    }

    public void setTime(LocalDateTime time) {
        configurableClock.setTime(time);
    }
}
