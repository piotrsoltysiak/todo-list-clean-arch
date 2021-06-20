package com.piotrsoltysiak.todoclean.application.ports.outbound;

import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;

public class TodoListNotFoundException extends RuntimeException {

    public TodoListNotFoundException(TodoListId todoListId) {
        super("Todo list not found for id: " + todoListId);
    }
}
