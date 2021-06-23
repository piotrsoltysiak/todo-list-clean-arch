package com.todoclean.domain.todolist;

public interface CompletionRevertPolicy {

    void assertRevertable(TodoItem todoItem);
}
