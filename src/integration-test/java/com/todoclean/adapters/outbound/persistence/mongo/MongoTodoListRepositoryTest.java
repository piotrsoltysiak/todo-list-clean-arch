package com.todoclean.adapters.outbound.persistence.mongo;

import com.todoclean.domain.todolist.TodoItem;
import com.todoclean.domain.todolist.TodoItemId;
import com.todoclean.domain.todolist.TodoList;
import com.todoclean.domain.todolist.TodoListId;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class MongoTodoListRepositoryTest extends MongoRepositoryTest {

    @Autowired
    private MongoTodoListRepository mongoTodoListRepository;

    @Test
    void should_properly_persist_and_load_todolist() {
        // given
        TodoListId todoListId = new TodoListId("test-todo-list-id");
        TodoList todoList = TodoList.create(todoListId, "Test todo list");
        mongoTodoListRepository.persist(todoList);

        // when
        TodoList persistedTodoList = mongoTodoListRepository.loadBy(todoListId);

        // then
        assertThat(todoList).isEqualTo(persistedTodoList);
    }

    // Todo write better test, think if update is a good idea?
    @Test
    void should_properly_update_todolist() {
        // given
        TodoListId todoListId = new TodoListId("test-todo-list-id");
        TodoList todoList = TodoList.create(todoListId, "Test todo list");
        LocalDateTime createdAt = LocalDateTime.of(2021, 6, 24, 10, 15);
        TodoItem todoItem = new TodoItem(new TodoItemId("test-todo-item-id"), "todo item description", createdAt);

        mongoTodoListRepository.persist(todoList);

        todoList.add(todoItem);
        mongoTodoListRepository.update(todoListId, list -> list.add(todoItem));

        // when
        TodoList persistedTodoList = mongoTodoListRepository.loadBy(todoListId);

        // then
        assertThat(todoList).isEqualTo(persistedTodoList);
    }

}
