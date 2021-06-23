package com.todoclean.domain.todolist;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

class TodoItemTest {

    @Test
    void should_mark_todo_item_as_completed() {
        // Given
        TodoItem todoItem = new TodoItem(
                new TodoItemId("todo-item-id"),
                "test the todo",
                LocalDateTime.now().minusSeconds(1)
        );
        LocalDateTime completedAt = LocalDateTime.now();
        todoItem.complete(completedAt);

        // When
        todoItem.complete(completedAt);

        // Then
        todoItem.isCompleted();
    }
}