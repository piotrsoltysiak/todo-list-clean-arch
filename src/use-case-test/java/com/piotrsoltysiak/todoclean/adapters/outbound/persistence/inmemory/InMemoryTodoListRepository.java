package com.piotrsoltysiak.todoclean.adapters.outbound.persistence.inmemory;

import com.piotrsoltysiak.todoclean.application.ports.outbound.TodoListNotFoundException;
import com.piotrsoltysiak.todoclean.application.ports.outbound.TodoListRepository;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoList;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class InMemoryTodoListRepository implements TodoListRepository {

    private static final ConcurrentHashMap<TodoListId, TodoList> records = new ConcurrentHashMap<>();

    @Override
    public TodoList loadBy(TodoListId todoListId) throws TodoListNotFoundException {
        if (!records.containsKey(todoListId)) {
            throw new TodoListNotFoundException(todoListId);
        }

        return records.get(todoListId);
    }

    @Override
    public void update(TodoListId id, Consumer<TodoList> action) throws TodoListNotFoundException {
        TodoList todoList = loadBy(id);
        action.accept(todoList);
        persist(todoList);
    }

    @Override
    public TodoList persist(TodoList todoList) {
        return records.put(todoList.getId(), todoList);
    }

    @Override
    public void deleteById(TodoListId todoListId) {
        records.remove(todoListId);
    }
}
