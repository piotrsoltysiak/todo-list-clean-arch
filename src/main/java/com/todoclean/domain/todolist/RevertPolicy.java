package com.todoclean.domain.todolist;

public interface RevertPolicy {

    void assertRevertable(TodoItem todoItem);
}
