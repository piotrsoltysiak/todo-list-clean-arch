package com.piotrsoltysiak.todoclean.domain.todolist;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TodoItem {

    private TodoItemId id;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    public TodoItem(TodoItemId id, String description, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.createdAt = createdAt;
    }

    public boolean hasId(TodoItemId todoItemId) {
        return id.equals(todoItemId);
    }

    public void complete(LocalDateTime completedAt) {
        if (isCompleted()) {
            return;
        }
        this.completedAt = completedAt;
    }

    public boolean isCompleted() {
        return completedAt != null;
    }

    void revertCompletion(RevertPolicy policy) {
        if (!isCompleted()) {
            return;
        }
        policy.assertRevertable(this);
        this.completedAt = null;
    }

    public boolean hasBenCompletedAfter(LocalDateTime time) {
        return completedAt.isAfter(time);
    }
}
