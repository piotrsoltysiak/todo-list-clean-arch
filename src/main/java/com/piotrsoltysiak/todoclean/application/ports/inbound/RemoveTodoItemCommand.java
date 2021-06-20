package com.piotrsoltysiak.todoclean.application.ports.inbound;

import com.piotrsoltysiak.todoclean.domain.todolist.TodoItemId;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;

import lombok.Value;

@Value
public class RemoveTodoItemCommand {
    TodoListId todoListId;
    TodoItemId todoItemId;
}
