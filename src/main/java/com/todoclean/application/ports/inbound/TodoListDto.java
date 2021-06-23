package com.todoclean.application.ports.inbound;

import com.todoclean.domain.todolist.TodoItem;
import com.todoclean.domain.todolist.TodoList;
import com.todoclean.domain.todolist.TodoListId;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class TodoListDto {

    TodoListId id;

    String title;

    List<TodoItemDto> todoItems;

    List<TodoItemDto> completedItems;

    public static TodoListDto from(TodoList todoList) {
        return new TodoListDto(
                todoList.getId(),
                todoList.getTitle(),
                toDtos(todoList.getTodoItems()),
                toDtos(todoList.getCompletedItems())
        );
    }

    private static List<TodoItemDto> toDtos(List<TodoItem> items) {
        return items.stream()
                .map(TodoItemDto::from)
                .collect(Collectors.toList());
    }

}
