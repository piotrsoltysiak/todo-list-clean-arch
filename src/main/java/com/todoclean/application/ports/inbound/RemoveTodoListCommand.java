package com.todoclean.application.ports.inbound;

import com.todoclean.domain.todolist.TodoListId;

import lombok.NonNull;
import lombok.Value;

@Value
public class RemoveTodoListCommand {
    @NonNull TodoListId todoListId;
}
