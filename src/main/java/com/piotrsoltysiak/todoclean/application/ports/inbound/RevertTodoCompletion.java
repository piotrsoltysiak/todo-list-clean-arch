package com.piotrsoltysiak.todoclean.application.ports.inbound;

public interface RevertTodoCompletion {

    void handle(RevertTodoCompletionCommand command);

}
