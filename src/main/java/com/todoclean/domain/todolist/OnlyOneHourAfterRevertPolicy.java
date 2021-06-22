package com.todoclean.domain.todolist;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnlyOneHourAfterRevertPolicy implements RevertPolicy {

    private final LocalDateTime revertedAt;

    @Override
    public void assertRevertable(TodoItem todoItem) {
        LocalDateTime lastPossibleRevertTime = revertedAt.minusHours(1);
        if (todoItem.hasBenCompletedAfter(lastPossibleRevertTime)) {
            return;
        }
        throw new IllegalStateException(
                String.format("Unable to revert, todo item should be completed less than one hour ago. Completed at:%s, reverted at: %s",
                        todoItem.getCompletedAt(), revertedAt));
    }
}
