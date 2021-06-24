package com.todoclean.adapters.inbound;

import com.todoclean.application.TestTodoListFacade;
import com.todoclean.application.ports.outbound.TodoListNotFoundException;
import com.todoclean.domain.todolist.TodoItemId;
import com.todoclean.domain.todolist.TodoItemNotFoundException;
import com.todoclean.domain.todolist.TodoListId;
import io.cucumber.java8.En;

import lombok.RequiredArgsConstructor;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RequiredArgsConstructor
public class TodoListAssertions implements En {

    public TodoListAssertions(TestTodoListFacade todoListFacade,
                              TestErrorHandler errorHandler) {
        Then("The item {string} is on the completed section of list {string}", (String todoDescription, String todoListTitle) -> {
            TodoListId todoListId = new TodoListId(toId(todoListTitle));
            TodoItemId todoItemId = new TodoItemId(toId(todoDescription));
            boolean isPresentOnTheCompletedList = isPresentOnTheCompletedList(todoListFacade, todoListId, todoItemId);
            assertThat(isPresentOnTheCompletedList).isTrue();
        });

        Then("The item {string} is not on the todo section of list {string}", (String todoDescription, String todoListTitle) -> {
            TodoListId todoListId = new TodoListId(toId(todoListTitle));
            TodoItemId todoItemId = new TodoItemId(toId(todoDescription));
            boolean isPresentOnThePendingList = isPresentOnThePendingList(todoListFacade, todoListId, todoItemId);
            assertThat(isPresentOnThePendingList).isFalse();
        });

        Then("The todo list {string} has no todo items", (String todoListTitle) -> {
            TodoListId todoListId = new TodoListId(toId(todoListTitle));
            boolean isPendingListEmpty = todoListFacade.findBy(todoListId)
                    .getTodoItems()
                    .isEmpty();

            assertThat(isPendingListEmpty);
        });

        Then("The todo list {string} has no completed items", (String todoListTitle) -> {
            TodoListId todoListId = new TodoListId(toId(todoListTitle));
            boolean isCompletedListEmpty = todoListFacade.findBy(todoListId)
                    .getCompletedItems()
                    .isEmpty();

            assertThat(isCompletedListEmpty);
        });

        Then("The item {string} is on the todo section of list {string}", (String todoDescription, String todoListTitle) -> {
            TodoListId todoListId = new TodoListId(toId(todoListTitle));
            TodoItemId todoItemId = new TodoItemId(toId(todoDescription));
            boolean isPresentOnThePendingList = isPresentOnThePendingList(todoListFacade, todoListId, todoItemId);

            assertThat(isPresentOnThePendingList).isTrue();
        });

        Then("The item {string} is not on the completed section of list {string}", (String todoDescription, String todoListTitle) -> {
            TodoListId todoListId = new TodoListId(toId(todoListTitle));
            TodoItemId todoItemId = new TodoItemId(toId(todoDescription));
            boolean isPresentOnTheCompletedList = isPresentOnTheCompletedList(todoListFacade, todoListId, todoItemId);

            assertThat(isPresentOnTheCompletedList).isFalse();
        });

        Then("Todo list {string} does not exist", (String todoListTitle) -> {
            String todoListIdRaw = toId(todoListTitle);
            TodoListId todoListId = new TodoListId(todoListIdRaw);
            Throwable throwable = catchThrowable(() -> todoListFacade.findBy(todoListId));
            assertThat(throwable)
                    .isInstanceOf(TodoListNotFoundException.class)
                    .hasMessageContaining(todoListIdRaw);
        });

        Then("Todo item not found error occurs", () -> errorHandler.assertThrown(TodoItemNotFoundException.class));

    }

    private boolean isPresentOnTheCompletedList(TestTodoListFacade todoListFacade, TodoListId todoListId, TodoItemId todoItemId) {
        return todoListFacade.findBy(todoListId)
                .getCompletedItems()
                .stream()
                .anyMatch(todoItemDto -> todoItemDto.getId().equals(todoItemId));
    }

    private boolean isPresentOnThePendingList(TestTodoListFacade todoListFacade, TodoListId todoListId, TodoItemId todoItemId) {
        return todoListFacade.findBy(todoListId)
                .getTodoItems()
                .stream()
                .anyMatch(todoItemDto -> todoItemDto.getId().equals(todoItemId));
    }

    private String toId(String todoListTitle) {
        return todoListTitle;
    }
}
