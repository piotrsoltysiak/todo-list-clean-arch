package com.piotrsoltysiak.todoclean.application.ports.inbound;

import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;

public interface FindTodoListById {

    TodoListDto findBy(TodoListId todoListId);

}
