package com.todoclean.adapters.inbound.rest;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.StateChangeAction;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import com.todoclean.application.TodoListFacade;
import com.todoclean.application.ports.inbound.CompleteTodoCommand;
import com.todoclean.application.ports.inbound.CreateTodoCommand;
import com.todoclean.application.ports.inbound.CreateTodoListCommand;
import com.todoclean.application.ports.inbound.RemoveTodoItemCommand;
import com.todoclean.application.ports.inbound.RemoveTodoListCommand;
import com.todoclean.application.ports.inbound.RevertTodoCompletionCommand;
import com.todoclean.domain.todolist.TodoItemId;
import com.todoclean.domain.todolist.TodoListId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Provider("TodoListBackend")
@PactFolder("pacts")
@WebMvcTest(
        controllers = TodoListController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class
        }
)
class ContractTestsRunner {

    private static final TodoListId TODO_LIST_ID = new TodoListId("test-todo-list-id");

    private static final TodoItemId TODO_ITEM_ID = new TodoItemId("test-todo-item-id");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoListFacade todoListFacade;

    @MockBean
    private Supplier<TodoListId> todoListIdSupplier;

    @MockBean
    private Supplier<TodoItemId> todoItemIdSupplier;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerification(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new MockMvcTestTarget(mockMvc));

    }

    @State(value = "Todo list with todo item which can be completed", action = StateChangeAction.TEARDOWN)
    void successfulCompleteTodoItemVerification() {
        CompleteTodoCommand command = new CompleteTodoCommand(TODO_LIST_ID, TODO_ITEM_ID);

        verify(todoListFacade).handle(command);
    }

    @State(value = "Allowing to create todo list")
    void successfulCreateTodoListSetup() {
        when(todoListIdSupplier.get()).thenReturn(TODO_LIST_ID);
    }

    @State(value = "Allowing to create todo list", action = StateChangeAction.TEARDOWN)
    void successfulCreateTodoListVerification() {
        CreateTodoListCommand command = new CreateTodoListCommand(TODO_LIST_ID, "Test todo list");

        verify(todoListFacade).handle(command);
    }

    @State(value = "Allowing to create todo item for given list id")
    void successfulCreateTodoItemSetup() {
        when(todoItemIdSupplier.get()).thenReturn(TODO_ITEM_ID);
    }

    @State(value = "Allowing to create todo item for given list id", action = StateChangeAction.TEARDOWN)
    void successfulCreateTodoItemVerification() {
        CreateTodoCommand command = new CreateTodoCommand(TODO_LIST_ID, TODO_ITEM_ID, "Test todo item");

        verify(todoListFacade).handle(command);
    }

    @State(value = "Allowing to remove todo item for given id and list id", action = StateChangeAction.TEARDOWN)
    void successfulRemoveTodoItemVerification() {
        RemoveTodoItemCommand command = new RemoveTodoItemCommand(TODO_LIST_ID, TODO_ITEM_ID);

        verify(todoListFacade).handle(command);
    }

    @State(value = "Allowing to remove todo list for given id", action = StateChangeAction.TEARDOWN)
    void successfulRemoveTodoListVerification() {
        RemoveTodoListCommand command = new RemoveTodoListCommand(TODO_LIST_ID);

        verify(todoListFacade).handle(command);
    }

    @State(value = "Allowing to revert completion of todo item for given id and list id", action = StateChangeAction.TEARDOWN)
    void successfulRevertTodoItemCompletionVerification() {
        RevertTodoCompletionCommand command = new RevertTodoCompletionCommand(TODO_LIST_ID, TODO_ITEM_ID);

        verify(todoListFacade).handle(command);
    }

}
