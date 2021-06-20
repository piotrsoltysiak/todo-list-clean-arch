package com.piotrsoltysiak.todoclean.adapters.inbound;

import com.piotrsoltysiak.todoclean.application.ports.inbound.RemoveTodoItemCommand;
import com.piotrsoltysiak.todoclean.application.TestTodoListFacade;
import com.piotrsoltysiak.todoclean.application.ports.inbound.CompleteTodoCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.CreateTodoCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.CreateTodoListCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.RemoveTodoListCommand;
import com.piotrsoltysiak.todoclean.application.ports.inbound.RevertTodoCompletionCommand;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoItemId;
import com.piotrsoltysiak.todoclean.domain.todolist.TodoListId;
import io.cucumber.java8.En;
import java.security.NoSuchAlgorithmException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TodoSteps implements En {

    public TodoSteps(TestTodoListFacade todoListFacade) {
        Given("I created todo list {string}", (String todoListTitle) -> {
            CreateTodoListCommand command = new CreateTodoListCommand(
                    new TodoListId(toId(todoListTitle)),
                    todoListTitle
            );
            todoListFacade.handle(command);
        });

        And("I created todo item {string} on list {string}", (String todoDescription, String todoListTitle) -> {
            CreateTodoCommand command = new CreateTodoCommand(
                    new TodoListId(toId(todoListTitle)),
                    new TodoItemId(toId(todoDescription)),
                    todoDescription
            );
            todoListFacade.handle(command);
        });

        When("I complete the item {string} from list {string}", (String todoDescription, String todoListTitle) -> {
            CompleteTodoCommand command = new CompleteTodoCommand(
                    new TodoListId(toId(todoListTitle)),
                    new TodoItemId(toId(todoDescription))
            );
            todoListFacade.handle(command);
        });

        When("I removed todo item {string} from list {string}", (String todoDescription, String todoListTitle) -> {
            RemoveTodoItemCommand command = new RemoveTodoItemCommand(
                    new TodoListId(toId(todoListTitle)),
                    new TodoItemId(toId(todoDescription))
            );

            todoListFacade.handle(command);
        });

        When("I revert the completion of item {string} from list {string}", (String todoDescription, String todoListTitle) -> {
            RevertTodoCompletionCommand command = new RevertTodoCompletionCommand(
                    new TodoListId(toId(todoListTitle)),
                    new TodoItemId(toId(todoDescription))
            );

            todoListFacade.handle(command);
        });

        When("I removed todo list {string}", (String todoListTitle) -> {
            RemoveTodoListCommand command = new RemoveTodoListCommand(
                    new TodoListId(toId(todoListTitle))
            );

            todoListFacade.handle(command);
        });
    }

    private String toId(String todoListName) throws NoSuchAlgorithmException {
        return todoListName;
        //return new String(MessageDigest.getInstance("MD5").digest(todoListName.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}
