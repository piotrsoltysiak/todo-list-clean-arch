package com.todoclean.domain.todolist;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class OnlyOneHourAfterCompletionRevertPolicyTest {

    @Test
    void should_pass_if_todo_is_completed_less_than_one_hour_before_revert() {
        // given
        LocalDateTime createdAt = LocalDateTime.now().minusMinutes(2);
        LocalDateTime completedAt = LocalDateTime.now().minusMinutes(1);
        TodoItem todoItem = new TodoItem(new TodoItemId("test-todo-item"), "Test todo item", createdAt);
        todoItem.complete(completedAt);

        // when
        Throwable throwable = catchThrowable(() -> new OnlyOneHourAfterRevertPolicy(LocalDateTime.now()).assertRevertable(todoItem));

        // then
        assertThat(throwable).isNull();
    }

    @Test
    void should_throw_exception_if_todo_is_completed_exactly_one_hour_before_revert() {
        // given
        LocalDateTime revertedAt = LocalDateTime.now();
        LocalDateTime createdAt = revertedAt.minusHours(2);
        LocalDateTime completedAt = revertedAt.minusHours(1);
        TodoItem todoItem = new TodoItem(new TodoItemId("test-todo-item"), "Test todo item", createdAt);
        todoItem.complete(completedAt);

        // when
        Throwable throwable = catchThrowable(() -> new OnlyOneHourAfterRevertPolicy(revertedAt).assertRevertable(todoItem));

        // then
        assertThat(throwable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unable to revert")
                .hasMessageContaining(String.valueOf(completedAt))
                .hasMessageContaining(String.valueOf(revertedAt));
    }

    @Test
    void should_throw_exception_if_todo_is_completed_more_than_one_hour_before_revert() {
        // given
        LocalDateTime revertedAt = LocalDateTime.now();
        LocalDateTime createdAt = revertedAt.minusHours(2);
        LocalDateTime completedAt = revertedAt.minusHours(1).minusSeconds(1);
        TodoItem todoItem = new TodoItem(new TodoItemId("test-todo-item"), "Test todo item", createdAt);
        todoItem.complete(completedAt);
        OnlyOneHourAfterRevertPolicy revertPolicy = new OnlyOneHourAfterRevertPolicy(revertedAt);

        // when
        Throwable throwable = catchThrowable(() -> revertPolicy.assertRevertable(todoItem));

        // then
        assertThat(throwable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unable to revert")
                .hasMessageContaining(String.valueOf(completedAt))
                .hasMessageContaining(String.valueOf(revertedAt));
    }
}