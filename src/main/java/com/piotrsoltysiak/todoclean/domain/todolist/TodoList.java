package com.piotrsoltysiak.todoclean.domain.todolist;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TodoList {
    private TodoListId id;
    private String title;
    private List<TodoItem> items;

    public static TodoList create(TodoListId todoListId, String title) {
        TodoList todoList = new TodoList();
        todoList.id = todoListId;
        todoList.title = title;
        todoList.items = new ArrayList<>();
        return todoList;
    }

    public void add(TodoItem todoItem) {
        items.add(todoItem);
    }

    public void remove(TodoItemId todoItemId) {
        items.removeIf(todoItem -> todoItem.hasId(todoItemId));
    }

    public void complete(TodoItemId todoItemId,
                         LocalDateTime completedAt) {
        items.stream()
                .filter(todoItem -> todoItem.hasId(todoItemId))
                .forEach(todoItem -> todoItem.complete(completedAt));
    }

    public void revertCompletion(TodoItemId todoItemId, RevertPolicy revertPolicy) {
        items.stream()
                .filter(todoItem -> todoItem.hasId(todoItemId))
                .forEach(todoItem -> todoItem.revertCompletion(revertPolicy));
    }

    private boolean allCompleted() {
        return items.stream().allMatch(TodoItem::isCompleted);
    }

}
