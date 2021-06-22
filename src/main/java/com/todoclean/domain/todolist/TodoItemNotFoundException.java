package com.todoclean.domain.todolist;

public class TodoItemNotFoundException extends RuntimeException {

    public TodoItemNotFoundException(TodoListId todoListId, TodoItemId todoItemId) {
        super(String.format("Todo item %s not found on todo list %s", todoItemId, todoListId));
    }
}
