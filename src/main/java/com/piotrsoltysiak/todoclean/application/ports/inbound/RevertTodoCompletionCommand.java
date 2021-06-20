package com.piotrsoltysiak.todoclean.application.ports.inbound;

import com.piotrsoltysiak.todoclean.domain.todolist.TodoItemId;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;

import java.time.LocalDateTime;

import lombok.Value;

@Value
public class RevertTodoCompletionCommand {
    TodoListId todoListId;
    TodoItemId todoItemId;
}
