package com.todoclean.adapters.outbound.persistence.mongo;

import com.todoclean.domain.todolist.TodoList;
import com.todoclean.domain.todolist.TodoListId;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;


import static org.assertj.core.api.Assertions.assertThat;

public class MongoTodoListRepositoryTest extends MongoRepositoryTest {

    @Autowired
    private MongoTodoListRepository mongoTodoListRepository;

    @Test
    void persisted_object_should_be_equal_to_the_one_before_persist() {
        // given
        TodoListId todoListId = new TodoListId("test-todo-list-id");
        TodoList todoList = TodoList.create(todoListId, "Test todo list");
        mongoTodoListRepository.persist(todoList);

        // when
        TodoList persistedTodoList = mongoTodoListRepository.loadBy(todoListId);

        // then
        assertThat(todoList).isEqualTo(persistedTodoList);
    }

}
