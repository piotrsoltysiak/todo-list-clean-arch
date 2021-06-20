package com.piotrsoltysiak.todoclean.application;

import com.piotrsoltysiak.todoclean.application.ports.inbound.CompleteTodoCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.CreateTodoCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.CreateTodoListCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.RemoveTodoItemCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.RemoveTodoListCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.RevertTodoCompletionCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.TodoListDto;
import com.piotrsoltysiak.todoclean.application.ports.outbound.TodoListRepository;
import com.piotrsoltysiak.todoclean.domain.todolist.OnlyOneHourAfterRevertPolicy;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoItem;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoList;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class TodoListService {
    private final TodoListRepository todoListRepository;
    private final Supplier<LocalDateTime> currentTimeSupplier;

    void handle(CompleteTodoCommand command) {
        // authorize
        todoListRepository.update(
                command.getTodoListId(),
                todoList -> todoList.complete(
                        command.getTodoItemId(),
                        currentTimeSupplier.get())
        );
    }

    void handle(RevertTodoCompletionCommand command) {
        // authorize
        todoListRepository.update(
                command.getTodoListId(),
                todoList -> todoList.revertCompletion(
                        command.getTodoItemId(),
                        new OnlyOneHourAfterRevertPolicy(currentTimeSupplier.get()))
        );
    }

    TodoListDto getById(TodoListId todoListId) {
        // authorize
        TodoList todoList = todoListRepository.loadBy(todoListId);
        return TodoListDto.from(todoList);
    }

    public void handle(CreateTodoListCommand command) {
        TodoList todoList = TodoList.create(command.getId(), command.getTitle());
        todoListRepository.persist(todoList);
    }

    public void handle(CreateTodoCommand command) {
        TodoItem todoItem = new TodoItem(
                command.getTodoItemId(),
                command.getDescription(),
                currentTimeSupplier.get()
        );

        todoListRepository.update(
                command.getTodoListId(),
                todoList -> todoList.add(todoItem)
        );
    }

    public void handle(RemoveTodoItemCommand command) {
        todoListRepository.update(
                command.getTodoListId(),
                todoList -> todoList.remove(command.getTodoItemId())
        );
    }

    public void handle(RemoveTodoListCommand command) {
        todoListRepository.deleteById(command.getTodoListId());
    }
}
