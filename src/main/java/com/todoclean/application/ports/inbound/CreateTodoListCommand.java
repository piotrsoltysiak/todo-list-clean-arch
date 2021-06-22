package com.todoclean.application.ports.inbound;

import com.todoclean.domain.todolist.TodoListId;

import lombok.NonNull;
import lombok.Value;

@Value
public class CreateTodoListCommand {
    @NonNull TodoListId id;
    @NonNull String title;
}
