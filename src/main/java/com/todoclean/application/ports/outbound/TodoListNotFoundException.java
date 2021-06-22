package com.todoclean.application.ports.outbound;

import com.todoclean.domain.todolist.TodoListId;

public class TodoListNotFoundException extends RuntimeException {

    public TodoListNotFoundException(TodoListId todoListId) {
        super("Todo list not found for id: " + todoListId);
    }
}
