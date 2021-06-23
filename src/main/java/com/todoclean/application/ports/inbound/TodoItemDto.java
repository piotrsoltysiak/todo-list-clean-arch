package com.todoclean.application.ports.inbound;

import com.todoclean.domain.todolist.TodoItem;
import com.todoclean.domain.todolist.TodoItemId;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public final class TodoItemDto {

    private final TodoItemId id;

    private final String whatNeedsToBeDone;

    public static TodoItemDto from(TodoItem todoItem) {
        return new TodoItemDto(
                todoItem.getId(),
                todoItem.getDescription()
        );
    }
}
