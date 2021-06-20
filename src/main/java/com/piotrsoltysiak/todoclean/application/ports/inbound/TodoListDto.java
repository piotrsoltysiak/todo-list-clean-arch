package com.piotrsoltysiak.todoclean.application.ports.inbound;

import com.piotrsoltysiak.todoclean.domain.todolist.TodoItem;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoList;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Value;

@Value
public class TodoListDto {
    TodoListId todoListId;
    String title;
    List<TodoItemDto> pendingItems;
    List<TodoItemDto> completedItems;

    public static TodoListDto from(TodoList todoList) {
        return new TodoListDto(
                todoList.getId(),
                todoList.getTitle(),
                pending(todoList.getItems()),
                completed(todoList.getItems())
        );
    }

    private static List<TodoItemDto> pending(List<TodoItem> items) {
        return items.stream()
                .filter(item -> !item.isCompleted())
                .map(TodoItemDto::from)
                .collect(Collectors.toList());
    }

    private static List<TodoItemDto> completed(List<TodoItem> items) {
        return items.stream()
                .filter(TodoItem::isCompleted)
                .map(TodoItemDto::from)
                .collect(Collectors.toList());
    }
}
