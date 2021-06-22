package com.todoclean.adapters.inbound;

import com.todoclean.application.TestTodoListFacade;
import com.todoclean.application.ports.inbound.CompleteTodoCommand;
import com.todoclean.application.ports.outbound.TodoListNotFoundException;
import com.todoclean.domain.todolist.TodoItemId;
import com.todoclean.domain.todolist.TodoItemNotFoundException;
import com.todoclean.domain.todolist.TodoListId;
import io.cucumber.java8.En;

import lombok.RequiredArgsConstructor;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RequiredArgsConstructor
public class TodoAssertions implements En {

    public TodoAssertions(TestTodoListFacade todoListFacade,
                          ErrorHandler errorHandler) {

        Then("The item {string} is on the completed section of list {string}", (String todoDescription, String todoListTitle) -> {
            TodoListId todoListId = new TodoListId(toId(todoListTitle));
            TodoItemId todoItemId = new TodoItemId(toId(todoDescription));
            boolean isPresentOnTheCompletedList = todoListFacade.findBy(todoListId)
                    .getCompletedItems()
                    .stream()
                    .anyMatch(todoItemDto -> todoItemDto.getId().equals(todoItemId) && todoItemDto.getWhatNeedsToBeDone().equals(todoDescription));
            assertThat(isPresentOnTheCompletedList).isTrue();
        });

        Then("The item {string} is not on the pending section of list {string}", (String todoDescription, String todoListTitle) -> {
            TodoListId todoListId = new TodoListId(toId(todoListTitle));
            TodoItemId todoItemId = new TodoItemId(toId(todoDescription));
            boolean isPresentOnThePendingList = todoListFacade.findBy(todoListId)
                    .getPendingItems()
                    .stream()
                    .anyMatch(todoItemDto -> todoItemDto.getId().equals(todoItemId));
            assertThat(isPresentOnThePendingList).isFalse();
        });

        Then("The todo list {string} has no pending items", (String todoListTitle) -> {
            TodoListId todoListId = new TodoListId(toId(todoListTitle));
            boolean isPendingListEmpty = todoListFacade.findBy(todoListId)
                    .getPendingItems()
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

        Then("The item {string} is on the pending section of list {string}", (String todoDescription, String todoListTitle) -> {
            TodoListId todoListId = new TodoListId(toId(todoListTitle));
            TodoItemId todoItemId = new TodoItemId(toId(todoDescription));
            boolean isPresentOnThePendingList = todoListFacade.findBy(todoListId)
                    .getPendingItems()
                    .stream()
                    .anyMatch(todoItemDto -> todoItemDto.getId().equals(todoItemId));

            assertThat(isPresentOnThePendingList).isTrue();
        });

        And("The item {string} is not on the completed section of list {string}", (String todoDescription, String todoListTitle) -> {
            TodoListId todoListId = new TodoListId(toId(todoListTitle));
            TodoItemId todoItemId = new TodoItemId(toId(todoDescription));
            boolean isPresentOnTheCompletedList = todoListFacade.findBy(todoListId)
                    .getCompletedItems()
                    .stream()
                    .anyMatch(todoItemDto -> todoItemDto.getId().equals(todoItemId) && todoItemDto.getWhatNeedsToBeDone().equals(todoDescription));

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

        When("I am unable to complete the item {string} from list {string} with not found error", (String todoDescription, String todoListTitle) -> {
            String todoItemIdRaw = toId(todoDescription);
            String todoListIdRaw = toId(todoListTitle);
            TodoListId todoListId = new TodoListId(todoListIdRaw);
            TodoItemId todoItemId = new TodoItemId(todoItemIdRaw);

            CompleteTodoCommand command = new CompleteTodoCommand(todoListId, todoItemId);

            Throwable throwable = catchThrowable(() -> todoListFacade.handle(command));

            assertThat(throwable)
                    .isInstanceOf(TodoItemNotFoundException.class)
                    .hasMessageContaining(todoItemIdRaw)
                    .hasMessageContaining(todoListIdRaw)
                    .hasMessageContaining("not found");
        });

        Then("Todo item not found error occurs", () -> errorHandler.assertThrown(TodoItemNotFoundException.class));

        Then("Revert outdated error occurs", () -> errorHandler.assertThrown(IllegalStateException.class)
                .hasMessageContaining("Unable to revert"));
    }

    private String toId(String todoListTitle) {
        return todoListTitle;
    }
}
