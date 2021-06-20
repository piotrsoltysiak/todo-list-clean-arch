package com.piotrsoltysiak.todoclean.domain.todolist;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.Value;

@Value
public class OnlyOneHourAfterRevertPolicy implements RevertPolicy {
    LocalDateTime revertedAt;

    @Override
    public void assertRevertable(TodoItem todoItem) {
        LocalDateTime lastPossibleRevertTime = revertedAt.minus(1, ChronoUnit.HOURS);
        if (todoItem.hasBenCompletedAfter(lastPossibleRevertTime)) {
            return;
        }
        throw new IllegalStateException();
    }
}
