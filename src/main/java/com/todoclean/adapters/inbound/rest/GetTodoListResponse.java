package com.todoclean.adapters.inbound.rest;

import com.todoclean.application.ports.inbound.TodoItemDto;
import com.todoclean.application.ports.inbound.TodoListDto;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
class GetTodoListResponse {

    private final String id;

    private final String title;

    private final List<GetTodoListResponseItem> todoItems;

    private final List<GetTodoListResponseItem> completedItems;

    public GetTodoListResponse(TodoListDto dto) {
        this.id = dto.getId().getRaw();
        this.title = dto.getTitle();
        this.todoItems = toResponseItem(dto.getTodoItems());
        this.completedItems = toResponseItem(dto.getCompletedItems());
    }

    private List<GetTodoListResponseItem> toResponseItem(List<TodoItemDto> todoItems) {
        return todoItems.stream()
                .map(GetTodoListResponseItem::new)
                .collect(Collectors.toList());
    }

    @Getter
    static class GetTodoListResponseItem {

        private final String id;

        private final String whatNeedsToBeDone;

        public GetTodoListResponseItem(TodoItemDto todoItemDto) {
            this.id = todoItemDto.getId().getRaw();
            this.whatNeedsToBeDone = todoItemDto.getWhatNeedsToBeDone();
        }
    }
}
