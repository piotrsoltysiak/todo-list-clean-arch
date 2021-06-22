package com.todoclean.application.ports.inbound;

import com.todoclean.domain.todolist.TodoItem;
import com.todoclean.domain.todolist.TodoList;
import com.todoclean.domain.todolist.TodoListId;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TodoListDto {

    TodoListId todoListId;

    String title;

    List<TodoItemDto> pendingItems;

    List<TodoItemDto> completedItems;

    public static TodoListDto from(TodoList todoList) {
        return new TodoListDto(
                todoList.getId(),
                todoList.getTitle(),
                toDtos(todoList.getPendingItems()),
                toDtos(todoList.getCompletedItems())
        );
    }

    private static List<TodoItemDto> toDtos(List<TodoItem> items) {
        return items.stream()
                .map(TodoItemDto::from)
                .collect(Collectors.toList());
    }

}
