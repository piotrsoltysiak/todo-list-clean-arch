package com.piotrsoltysiak.todoclean.application;

import com.piotrsoltysiak.todoclean.application.ports.inbound.CompleteTodo;
import com.piotrsoltysiak.todoclean.application.ports.inbound.CompleteTodoCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.CreateTodoCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.CreateTodoListCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.FindTodoListById;
import com.piotrsoltysiak.todoclean.application.ports.inbound.RemoveTodoItemCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.RemoveTodoListCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.RevertTodoCompletion;
import com.piotrsoltysiak.todoclean.application.ports.inbound.RevertTodoCompletionCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.TodoListDto;
import com.piotrsoltysiak.todoclean.application.ports.outbound.CurrentTimeSupplier;
import com.piotrsoltysiak.todoclean.application.ports.outbound.TodoListRepository;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TodoListFacade implements CompleteTodo, RevertTodoCompletion, FindTodoListById {
    private final TodoListRepository todoListRepository;
    private final CurrentTimeSupplier currentTimeSupplier;

    @Override
    public void handle(CompleteTodoCommand command) {
        todoListService().handle(command);
    }

    private TodoListService todoListService() {
        return new TodoListService(todoListRepository, currentTimeSupplier);
    }

    @Override
    public void handle(RevertTodoCompletionCommand command) {
        todoListService().handle(command);
    }

    @Override
    public TodoListDto findBy(TodoListId todoListId) {
        return todoListService().getById(todoListId);
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
}
