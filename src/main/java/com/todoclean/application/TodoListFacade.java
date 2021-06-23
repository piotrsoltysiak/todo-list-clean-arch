package com.todoclean.application;

import com.todoclean.application.ports.inbound.CompleteTodoCommand;
import com.todoclean.application.ports.inbound.CreateTodoCommand;
import com.todoclean.application.ports.inbound.CreateTodoListCommand;
import com.todoclean.application.ports.inbound.RemoveTodoItemCommand;
import com.todoclean.application.ports.inbound.RemoveTodoListCommand;
import com.todoclean.application.ports.inbound.RevertTodoCompletionCommand;
import com.todoclean.application.ports.inbound.TodoListDto;
import com.todoclean.application.ports.outbound.CurrentTimeSupplier;
import com.todoclean.application.ports.outbound.TodoListRepository;
import com.todoclean.domain.todolist.TodoListId;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TodoListFacade {

    private final TodoListRepository todoListRepository;

    private final CurrentTimeSupplier currentTimeSupplier;

    public void handle(CompleteTodoCommand command) {
        todoListService().handle(command);
    }

    private TodoListService todoListService() {
        return new TodoListService(todoListRepository, currentTimeSupplier);
    }

    public void handle(RevertTodoCompletionCommand command) {
        todoListService().handle(command);
    }

    public void handle(CreateTodoListCommand command) {
        todoListService().handle(command);
    }

    public void handle(CreateTodoCommand command) {
        todoListService().handle(command);
    }

    public void handle(RemoveTodoItemCommand command) {
        todoListService().handle(command);
    }

    public void handle(RemoveTodoListCommand command) {
        todoListService().handle(command);
    }

    public TodoListDto findBy(TodoListId todoListId) {
        return todoListService().findById(todoListId);
    }
}
