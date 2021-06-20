package com.piotrsoltysiak.todoclean.application.ports.inbound;

import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;

import lombok.Value;

@Value
public class RemoveTodoListCommand {
    TodoListId todoListId;
}