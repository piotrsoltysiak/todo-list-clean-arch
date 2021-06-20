package com.piotrsoltysiak.todoclean.application.ports.inbound;

public interface CompleteTodo {

    void handle(CompleteTodoCommand command);

}
