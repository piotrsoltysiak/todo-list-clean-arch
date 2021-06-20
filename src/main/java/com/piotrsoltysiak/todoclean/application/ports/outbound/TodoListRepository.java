package com.piotrsoltysiak.todoclean.application.ports.outbound;

import com.piotrsoltysiak.todoclean.domain.todolist.TodoList;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;
import java.util.function.Consumer;

public interface TodoListRepository {

    TodoList loadBy(TodoListId todoListId) throws TodoListNotFoundException;

    void update(TodoListId id, Consumer<TodoList> action) throws TodoListNotFoundException;

    TodoList persist(TodoList todoList);

    void deleteById(TodoListId todoListId);
}
