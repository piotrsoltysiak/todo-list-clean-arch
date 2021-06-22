package com.todoclean.application.ports.inbound;

import com.todoclean.domain.todolist.TodoItemId;
import com.todoclean.domain.todolist.TodoListId;

import lombok.NonNull;
import lombok.Value;

@Value
public class CreateTodoCommand {
    @NonNull TodoListId todoListId;
    @NonNull TodoItemId todoItemId;
    @NonNull String description;
}
