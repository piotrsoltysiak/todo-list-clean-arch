package com.piotrsoltysiak.todoclean.domain.todolist;

public interface RevertPolicy {

    void assertRevertable(TodoItem todoItem);
}
