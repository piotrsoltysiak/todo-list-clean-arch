package com.piotrsoltysiak.todoclean.adapters.inbound;

import com.piotrsoltysiak.todoclean.application.TestTodoListFacade;
import com.piotrsoltysiak.todoclean.application.ports.outbound.TodoListNotFoundException;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoItemId;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;
import io.cucumber.java8.En;
import java.security.NoSuchAlgorithmException;

import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RequiredArgsConstructor
public class TodoAssertions implements En {

    public TodoAssertions(TestTodoListFacade todoListFacade) {
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
    }

    private String toId(String todoListTitle) throws NoSuchAlgorithmException {
        return todoListTitle;
        //return new String(MessageDigest.getInstance("MD5").digest(todoListName.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}
