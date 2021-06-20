package com.piotrsoltysiak.todoclean.application.ports.inbound;

import com.piotrsoltysiak.todoclean.domain.todolist.TodoItem;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoItemId;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TodoItemDto {

    private final TodoItemId id;

    private final String whatNeedsToBeDone;

    public static TodoItemDto from(TodoItem todoItem) {
        return new TodoItemDto(
                todoItem.getId(),
                todoItem.getDescription()
        );
    }
}
