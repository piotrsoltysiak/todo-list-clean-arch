package com.todoclean.adapters.inbound.rest;

import com.todoclean.application.TodoListFacade;
import com.todoclean.application.ports.inbound.CompleteTodoCommand;
import com.todoclean.application.ports.inbound.CreateTodoCommand;
import com.todoclean.application.ports.inbound.CreateTodoListCommand;
import com.todoclean.application.ports.inbound.RemoveTodoItemCommand;
import com.todoclean.application.ports.inbound.RemoveTodoListCommand;
import com.todoclean.application.ports.inbound.RevertTodoCompletionCommand;
import com.todoclean.application.ports.inbound.TodoListDto;
import com.todoclean.domain.todolist.TodoItemId;
import com.todoclean.domain.todolist.TodoListId;
import java.net.URI;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/todo-lists")
@RequiredArgsConstructor
class TodoListController {

    private final TodoListFacade todoListFacade;

    private final Supplier<TodoListId> todoListIdSupplier;

    private final Supplier<TodoItemId> todoItemIdSupplier;

    @PostMapping
    ResponseEntity<Void> createTodoList(@RequestBody CreateTodoListRequest request) {
        CreateTodoListCommand command = new CreateTodoListCommand(
                todoListIdSupplier.get(),
                request.getName()
        );

        URI location = UriResolver
                .operateOnPath("/todo-lists/{todo-list-id}")
                .andResolveWithIds(command.getId().getRaw());

        todoListFacade.handle(command);

        return ResponseEntity
                .created(location)
                .build();
    }

    @PostMapping("/{list-id}/items")
    ResponseEntity<Void> createTodo(@PathVariable(value = "list-id") @NonNull String listId,
                                    @RequestBody CreateTodoRequest request) {
        CreateTodoCommand command = new CreateTodoCommand(
                new TodoListId(listId),
                todoItemIdSupplier.get(),
                request.getDescription()
        );

        URI location = UriResolver
                .operateOnPath("/todo-lists/{todo-list-id}/items/{todo-item-id}")
                .andResolveWithIds(
                        command.getTodoListId().getRaw(),
                        command.getTodoItemId().getRaw());

        todoListFacade.handle(command);

        return ResponseEntity
                .created(location)
                .build();
    }

    @PutMapping("/{list-id}/items/{item-id}/complete")
    @ResponseStatus(HttpStatus.OK)
    void completeTodo(@PathVariable(value = "list-id") @NonNull String listId,
                      @PathVariable(value = "item-id") @NonNull String itemId) {
        CompleteTodoCommand command = new CompleteTodoCommand(
                new TodoListId(listId),
                new TodoItemId(itemId)
        );

        todoListFacade.handle(command);
    }

    @PutMapping("/{list-id}/items/{item-id}/revert-completion")
    @ResponseStatus(HttpStatus.OK)
    void revertTodoItemCompletion(@PathVariable(value = "list-id") @NonNull String listId,
                                  @PathVariable(value = "item-id") @NonNull String itemId) {
        RevertTodoCompletionCommand command = new RevertTodoCompletionCommand(
                new TodoListId(listId),
                new TodoItemId(itemId)
        );

        todoListFacade.handle(command);
    }

    @DeleteMapping("/{list-id}/items/{item-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeTodoItem(@PathVariable(value = "list-id") @NonNull String listId,
                        @PathVariable(value = "item-id") @NonNull String itemId) {
        RemoveTodoItemCommand command = new RemoveTodoItemCommand(
                new TodoListId(listId),
                new TodoItemId(itemId)
        );

        todoListFacade.handle(command);
    }

    @DeleteMapping("/{list-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeTodoList(@PathVariable(value = "list-id") @NonNull String listId) {
        RemoveTodoListCommand command = new RemoveTodoListCommand(
                new TodoListId(listId)
        );

        todoListFacade.handle(command);
    }

    @GetMapping(value = "/{list-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    GetTodoListResponse getTodoList(@PathVariable(value = "list-id") @NonNull String listId) {
        TodoListId todoListId = new TodoListId(listId);
        TodoListDto dto = todoListFacade.findBy(todoListId);
        return new GetTodoListResponse(dto);
    }

}
