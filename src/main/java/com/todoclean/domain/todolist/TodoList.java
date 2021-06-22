package com.todoclean.domain.todolist;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        assertContains(todoItemId);

        items.stream()
                .filter(todoItem -> todoItem.hasId(todoItemId))
                .forEach(todoItem -> todoItem.complete(completedAt));
    }

    private void assertContains(TodoItemId todoItemId) {
        if (contains(todoItemId)) {
            return;
        }

        throw new TodoItemNotFoundException(id, todoItemId);
    }

    private boolean contains(TodoItemId todoItemId) {
        return items.stream()
                .map(TodoItem::getId)
                .anyMatch(todoItemId::equals);
    }

    public void revertCompletion(TodoItemId todoItemId, RevertPolicy revertPolicy) {
        items.stream()
                .filter(todoItem -> todoItem.hasId(todoItemId))
                .forEach(todoItem -> todoItem.revertCompletion(revertPolicy));
    }

    public List<TodoItem> getPendingItems() {
        return items.stream()
                .filter(item -> !item.isCompleted())
                .collect(Collectors.toList());
    }

    public List<TodoItem> getCompletedItems() {
        return items.stream()
                .filter(TodoItem::isCompleted)
                .collect(Collectors.toList());
    }
}
