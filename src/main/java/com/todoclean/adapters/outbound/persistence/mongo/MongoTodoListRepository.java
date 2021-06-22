package com.todoclean.adapters.outbound.persistence.mongo;

import com.todoclean.application.ports.outbound.TodoListNotFoundException;
import com.todoclean.application.ports.outbound.TodoListRepository;
import com.todoclean.domain.todolist.TodoList;
import com.todoclean.domain.todolist.TodoListId;
import java.util.function.Consumer;

import org.springframework.data.mongodb.repository.MongoRepository;

interface MongoTodoListRepository extends TodoListRepository, MongoRepository<TodoList, TodoListId> {

    @Override
    default TodoList loadBy(TodoListId id) throws TodoListNotFoundException {
        return this.findById(id)
                .orElseThrow(() -> new TodoListNotFoundException(id));
    }

    @Override
    default void update(TodoListId id, Consumer<TodoList> action) {
        // begin transaction
        this.findById(id)
                .ifPresentOrElse(
                        todoList -> {
                            action.accept(todoList);
                            persist(todoList);
                            // commit transaction
                        },
                        () -> {
                            throw new TodoListNotFoundException(id);
                            // abort transaction
                        }
                );
    }

    @Override
    default TodoList persist(TodoList todoList) {
        return this.save(todoList);
    }

}
