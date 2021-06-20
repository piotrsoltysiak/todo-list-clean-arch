package com.piotrsoltysiak.todoclean.application;

import com.piotrsoltysiak.todoclean.adapters.outbound.persistence.inmemory.InMemoryTodoListRepository;
import java.time.LocalDateTime;

public class TestTodoListFacade extends TodoListFacade {

    public TestTodoListFacade() {
        super(
                new InMemoryTodoListRepository(),
                LocalDateTime::now
        );
    }
}
