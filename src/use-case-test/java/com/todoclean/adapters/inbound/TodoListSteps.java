package com.todoclean.adapters.inbound;

import com.todoclean.application.TestTodoListFacade;
import com.todoclean.application.ports.inbound.CompleteTodoCommand;
import com.todoclean.application.ports.inbound.CreateTodoCommand;
import com.todoclean.application.ports.inbound.CreateTodoListCommand;
import com.todoclean.application.ports.inbound.RemoveTodoItemCommand;
import com.todoclean.application.ports.inbound.RemoveTodoListCommand;
import com.todoclean.application.ports.inbound.RevertTodoCompletionCommand;
import com.todoclean.domain.todolist.TodoItemId;
import com.todoclean.domain.todolist.TodoListId;
import io.cucumber.java8.En;

import lombok.RequiredArgsConstructor;

public class TodoListSteps implements En {

    public TodoListSteps(TestTodoListFacade todoListFacade,
                         ErrorHandler errorHandler) {

        Given("I created todo list {string}", (String todoListTitle) ->
                errorHandler.executeWithExceptionHandling(() -> {
                    CreateTodoListCommand command = new CreateTodoListCommand(
                            new TodoListId(toId(todoListTitle)),
                            todoListTitle
                    );
                    todoListFacade.handle(command);
                }));

        And("I created todo item {string} on list {string}", (String todoDescription, String todoListTitle) -> {
            errorHandler.executeWithExceptionHandling(() -> {
                CreateTodoCommand command = new CreateTodoCommand(
                        new TodoListId(toId(todoListTitle)),
                        new TodoItemId(toId(todoDescription)),
                        todoDescription
                );

                todoListFacade.handle(command);
            });
        });

        When("I complete the item {string} from list {string}", (String todoDescription, String todoListTitle) -> {
            errorHandler.executeWithExceptionHandling(() -> {
                CompleteTodoCommand command = new CompleteTodoCommand(
                        new TodoListId(toId(todoListTitle)),
                        new TodoItemId(toId(todoDescription))
                );

                todoListFacade.handle(command);
            });
        });

        When("I removed todo item {string} from list {string}", (String todoDescription, String todoListTitle) -> {
            errorHandler.executeWithExceptionHandling(() -> {
                RemoveTodoItemCommand command = new RemoveTodoItemCommand(
                        new TodoListId(toId(todoListTitle)),
                        new TodoItemId(toId(todoDescription))
                );

                todoListFacade.handle(command);
            });
        });

        When("I revert the completion of item {string} from list {string}", (String todoDescription, String todoListTitle) -> {
            errorHandler.executeWithExceptionHandling(() -> {
                RevertTodoCompletionCommand command = new RevertTodoCompletionCommand(
                        new TodoListId(toId(todoListTitle)),
                        new TodoItemId(toId(todoDescription))
                );

                todoListFacade.handle(command);
            });
        });

        When("I removed todo list {string}", (String todoListTitle) -> {
            errorHandler.executeWithExceptionHandling(() -> {
                RemoveTodoListCommand command = new RemoveTodoListCommand(
                        new TodoListId(toId(todoListTitle))
                );

                todoListFacade.handle(command);
            });
        });

    }

    private String toId(String todoListName) {
        return todoListName;
    }

}
