package com.piotrsoltysiak.todoclean.domain.todolist;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TodoItem {

    private final TodoItemId id;

    private final String description;

    private final LocalDateTime createdAt;

    private LocalDateTime completedAt;

    public TodoItem(TodoItemId id, String description, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.createdAt = createdAt;
    }

    boolean hasId(TodoItemId todoItemId) {
        return id.equals(todoItemId);
    }

    void complete(LocalDateTime completedAt) {
        if (isCompleted()) {
            return;
        }
        this.completedAt = completedAt;
    }

    boolean isCompleted() {
        return completedAt != null;
    }

    void revertCompletion(RevertPolicy policy) {
        if (!isCompleted()) {
            return;
        }
        policy.assertRevertable(this);
        this.completedAt = null;
    }

    boolean hasBenCompletedAfter(LocalDateTime time) {
        return completedAt.isAfter(time);
    }
}
