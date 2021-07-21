# Todo list with clean architecture - example project



### Architecture overview

The single, most important principle of clean architecture is [the dependency rule](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html):


 >Source code dependencies can only point inwards. Nothing in an inner circle can know anything at all about something in an outer circle. In particular, the name of something declared in an outer circle must not be mentioned by the code in the inner circle. That includes functions, classes. variables, or any other named software entity.

![Architecture overview](docs/images/overview.png)

It's easy to memorize the rule if we think about the architectural model as build not with circles, but as a tower of pucks. You should be able to use elements from higher "puck" any other tower without changing the puck. 

Translating it into code - you should be able to reuse domain objects in any use case without changing its code and it should give the same effect. You should be able to trigger the use case from multiple inputs (inbound adapters) and get the same results. And so on.

The number of circles may vary, depending on the needs.

In this project I've decided to go with the most common approach, consisting of four circles. Starting from the higher, most inner one:



#### Domain

The domain layer encapsulates the business logic, rules and processes. 

It's a place where all the tactical DDD building blocks belongs, such as:

- Aggregates
- Entities
- Value objects
- Policies
- Domain services
- Domain events
- Factories

Code in this layer should be technology independent, preferably written with plain Java (or any other language) code. However, the most pragmatic approach is to combine the domain objects with ORM annotations (if you are using an SQL database).



#### Application

The application layer encapsulates the use cases, by orchestrating the domain layer and exposing it using a well-defined API. It also defines the authorization rules for executing use cases.

The application layer, combined with the domain is often referred to as **the application core**. The only things application core exposes publicly are ports, which allows exposing two types of application features:

* capabilities - all use cases possible to execute, exposed by **inbound ports** (e.g - CreateTodoList) - marked as red circles on the figure below. The inbound ports are mostly **Commands**, used for changing the application state, and **Queries**, allowing to get the current state. It's a good practice to not combine those two (be [CQS](https://martinfowler.com/bliki/CommandQuerySeparation.html)-compliant).
* needs - everything needed by the application to execute use cases, exposed by **outbound ports** (e.g - LoadTodoListById) - marked as grey circles on the figure below

![Ports](docs/images/ports.png)

Ports can be implemented as well-separated interfaces, build around a single use case or single need. 

The other approach, which I find simpler and more practical is to build a facade around the application, exposing the inbound ports by its public methods. To construct such a facade, you need to provide the implementation of all outbound adapters, which makes it a single object you need to create to be able to run the application code and make it hard to create the application in the invalid state.

In this project the ports are exposed using facade:


![Facade](docs/images/facade.png)



#### Adapters

The adapters layer encapsulates the logic needed to adapt certain infrastructure to application ports. This layer contains code responsible for two things:

- exposing capabilities to the external world - through REST, SOAP, CLI and other interfaces adapters, called **inbound adapters**
- providing tools needed by application - by implementing **outbound adapters** to infrastructure

In this project, the only inbound adapter needed is the single REST controller - `TodoListController`. The application needs access to clock and persistence, so we have two outbound ports - `MongoTodoListRepository` and anonymous class providing time using method reference `LocalDateTime::now`.

Thanks to the dependency rule, we can replace any adapter, or add another one with no need to make any change in the application core. It might get a little more complicated if we decide to combine ORM entities with domain entities (and then change from SQL database to NoSQL database).


![Adapters](docs/images/adapters.png)


#### Infrastructure

Once again, [quoting Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html): 

>The outermost layer is generally composed of frameworks and tools such as the Database, the Web Framework, etc. Generally you don’t write much code in this layer other than glue code that communicates to the next circle inwards.
>
>This layer is where all the details go. The Web is a detail. The database is a detail. We keep these things on the outside where they can do little harm.



### Testing strategy

#### Domain

To test the domain layer, we can go with plain old simple [solitary unit tests](https://martinfowler.com/articles/practical-test-pyramid.html#SociableAndSolitary) :

```java
class TodoItemTest {

    @Test
    void should_mark_todo_item_as_completed() {
        // Given
        TodoItem todoItem = new TodoItem(
                new TodoItemId("todo-item-id"),
                "test the todo",
                LocalDateTime.now().minusSeconds(1)
        );
        LocalDateTime completedAt = LocalDateTime.now();
        todoItem.complete(completedAt);

        // When
        todoItem.complete(completedAt);

        // Then
        todoItem.isCompleted();
    }
}
```



#### Application

When it comes to testing use cases, we should interact with the application core in the same way the client does. 

First, we should focus on what the application should do, not how its implemented. To separate the "what" from "how", we can prepare human-readable test scenarios with [Gherkin](https://cucumber.io/docs/gherkin/), and make them executable with [Cucumber](https://cucumber.io/):

```gherkin
Feature: Complete Todo Item

  Scenario: Happy path
    Given I created todo list "Clean arch tech talk"
    And I created todo item "prepare the presentation" on list "Clean arch tech talk"
    When I complete the item "prepare the presentation" from list "Clean arch tech talk"
    Then The item "prepare the presentation" is on the completed section of list "Clean arch tech talk"
    And The item "prepare the presentation" is not on the todo section of list "Clean arch tech talk"
```

We should treat the Cucumber [steps definitions](https://cucumber.io/docs/cucumber/step-definitions/) as inbound adapters, and focus on adapting the [step expression](https://cucumber.io/docs/cucumber/step-definitions/#expressions) to the application facade:

```java
    When("I complete the item {string} from list {string}", 
         (String todoDescription, String todoListTitle) -> {
            errorHandler.executeWithExceptionHandling(() -> {
                CompleteTodoCommand command = new CompleteTodoCommand(
                        new TodoListId(toId(todoListTitle)),
                        new TodoItemId(toId(todoDescription))
                );

                todoListFacade.handle(command);
            });
        });
```

To make the test application work, we need to provide the outbound adapters. The easiest way is to simply write them all by yourself as simple in-memory implementations:

```java
public class InMemoryTodoListRepository implements TodoListRepository {

    private final ConcurrentHashMap<TodoListId, TodoList> records = new ConcurrentHashMap<>();

    @Override
    public TodoList loadBy(TodoListId todoListId) throws TodoListNotFoundException {
        if (!records.containsKey(todoListId)) {
            throw new TodoListNotFoundException(todoListId);
        }

        return records.get(todoListId);
    }
  
  // ...
}
```

And inject those implementations to the test application:

```java
public class TestTodoListFacade extends TodoListFacade {
    private final ConfigurableClock configurableClock;

    public TestTodoListFacade() {
        this(
                new InMemoryTodoListRepository(),
                new ConfigurableClock()
        );
    }

    public TestTodoListFacade(TodoListRepository todoListRepository,
                              ConfigurableClock configurableClock) {
        super(todoListRepository, configurableClock);
        this.configurableClock = configurableClock;
    }

    public void setTime(LocalDateTime time) {
        configurableClock.setTime(time);
    }
}
```

By extending the application facade we can ensure that the same code would work on local test environments and production.

To finish the puzzle, we use a lightweight application container - [Pico](http://picocontainer.com/) to take care of creating a new instance of the application for every scenario (no additional code needed) and inject it to Cucumber steps:

```java
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
      
      // ... 
    }
}
```

The only thing required by Pico to automatically create an instance of the component is a no-args constructor, otherwise, additional configuration code is needed. Every scenario runs on a brand new application and infrastructure. 

It seems a little complicated, but it's worth it. With this little effort we get:

- Human-readable specification, well separated from implementation
- Non-fragile tests, nearly as fast as unit tests (hundreds per second)
- Ability to refactor without effort and losing focus
- BDD framework allowing to focus on what the application should do, instead of how it does it
- Full flexibility on setting up the test environment behaviour.

We should take care of high coverage here, preferably 100% known use cases (and adding a new one every time corner case appears).



#### Adapters

The dependency rule makes the application not depend on the adapters layer. Thanks to that, we can test the adapters in isolation.



##### Inbound

The purpose of the inbound adapter is to receive some kind of signal from the infrastructure and adapt it to the port exposed by the application (e.g receive HTTP request from REST API and adapt it to facade method call).

What we want to test (and specify) here is if the signal is being properly adapted. Such tests should take care of contract consistency, so the clients won't get any unexpected changes. I believe the right approach is to execute the same signal being executed on production and verify if the right port has been called (with expected arguments). Thanks to complying with the dependency rule, we can stub the whole application without any risk. Furthermore, there is no need to test all the use cases - they are tested on the application layer level.

Some tools help us to minimise the implementation cost:

- spring slice tests - allowing to run spring with limited context (e.g [only the web layer](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-mvc-tests)), significantly reducing execution time 
- contract testing framework - responsible for triggering the signal, allowing to define contracts apart from the test code, e.g [pact](https://docs.pact.io/) 

![Inbound adapters test](docs/images/inbound-adapters-test.png)



Both tools are used in the example code:  


1. Pact triggers the request defined in interaction description
    ```json
    {
       "description":"A request to create todo list",
       "providerState":"Allowing to create todo list",
       "request":{
          "method":"POST",
          "path":"/todo-lists",
          "body":{
             "id":"test-todo-list-id",
             "name":"Test todo list"
          }
       },
       "response":{
          "status":201
       }
    }
    ```
1. Spring-mvc-slice test context handles it        
    ```java 
    @Provider("TodoListBackend")
    @PactFolder("pacts")
    @WebMvcTest(
            controllers = TodoListController.class,
            excludeAutoConfiguration = {
                    SecurityAutoConfiguration.class
            }
    )
    class ContractTestsRunner {
      
        @Autowired
        private MockMvc mockMvc;
    
        @MockBean
        private TodoListFacade todoListFacade;
    
        @TestTemplate
        @ExtendWith(PactVerificationInvocationContextProvider.class)
        void pactVerification(PactVerificationContext context) {
            context.verifyInteraction();
        }
    
        @BeforeEach
        void before(PactVerificationContext context) {
            context.setTarget(new MockMvcTestTarget(mockMvc));
        }
      
    }
    ```
1. (command case) Pact compares actual response with minimal expected response OR 
    ```java
    @State(value = "Allowing to create todo list", action = StateChangeAction.TEARDOWN)
    void successfulCreateTodoListVerification() {
        CreateTodoListCommand command = new CreateTodoListCommand(TODO_LIST_ID, "Test todo list");
    
        verify(todoListFacade).handle(command);
    }
    ```
1. (query case) Mockito verifies whether the right facade method had been called in expected arguments    
    ```java
    @State(value = "Allowing to get todo list for given id")
    void successfulGetTodoListByIdVerification() {
        TodoItemDto completedItem = new TodoItemDto(
                TODO_ITEM_ID,
                "Clean architecture presentation");
    
        when(todoListFacade.findBy(TODO_LIST_ID)).thenReturn(
                new TodoListDto(
                        TODO_LIST_ID,
                        "Test todo list",
                        Collections.emptyList(),
                        Collections.singletonList(completedItem)
        ));
    }
    ```

In case when we are unable to maintain contract tests using a dedicated framework, we should follow the same test flow, and only replace the test framework with a custom one, able to produce the same signal as expected on production. E.g if the application would expose CLI, I'd suggest writing tests that verify whether the commands provided by `stdin` result in triggering expected methods on the application facade. 

Thanks to testing the inbound adapters in isolation and mocking a single bean (application facade) the execution time should be close to the spring-slice-context bootstrap time (approx. few seconds).

​                                                                                                                  

##### Outbound   

Outbound adapters receive a signal from the application core and adapt them to the infrastructure capable of handling such signals properly.  As in the inbound ports, what we want to test (and specify) here is if the signal is being properly adapted. However in this case the signal comes from the application core, and we should verify whether it results in a specific outcome in infrastructure.

The nice feature of testing outbound adapters is that they are completely independent of the application core, so there is no mocking needed at all. We can simply take the adapter and test it in full isolation (but integrated with the infrastructure).

![Outboubd adapters test](docs/images/outbound-adapters-test.png)

**It's important to test the outbound adapters using infrastructure as similar to the production environment as possible.** If we use `PostgreSQL` on prod and `H2` for tests - we can get completely different results for both cases, which makes the test unreliable.

As for the inbound ports, there are also tools that helps to minimise the implementation cost:

- spring slice tests - allowing to run spring with limited context (e.g [only the MongoDB-related beans](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-mongo-test)), significantly reducing execution time 
- [test containers](https://www.testcontainers.org/), "providing lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container" - which helps us to perform using the real-life infrastructure.

To minimise tests execution time, we can create an abstract, base class - both spring context and test container would be bootstrapped only once, no matter how many test classes would inherit from it. 

```java
@Testcontainers
@DataMongoTest
@ContextConfiguration(classes = { TodoSpringApplication.class, MongoConfig.class })
public abstract class MongoRepositoryTest {

    @Rule
    protected static final MongoDBContainer MONGO = SharedTestMongoContainer.getInstance();
  
    // ...
  
}
```

In those tests, we should focus on verifying if the adapter works properly, not on business logic. For example:

- in persistence adapter we want to make sure if our code can save the entity, and find it using some custom query;
- in the messaging adapter we want to test if the correct message was sent, and one way to do it is to receive it and check if this is what we expected;

In this project we have a single mongo adapter (MongoTodoListRepository), with a simple test verifying whether we can persist and load the entity:

```java
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
```



#### Infrastructure

To properly test the infrastructure layer we should go with e2e tests, which are not covered in this repository.



#### Architecture

To ensure that all architecture rules are respected we can use [ArchUnit](https://www.archunit.org/):

```java
    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .layer(DOMAIN_LAYER).definedBy(DOMAIN_LAYER_PACKAGE)
            .layer(APPLICATION_LAYER).definedBy(APPLICATION_LAYER_PACKAGE)
            .layer(ADAPTERS_LAYER).definedBy(ADAPTERS_LAYER_PACKAGE)
            .whereLayer(ADAPTERS_LAYER).mayNotBeAccessedByAnyLayer()
            .whereLayer(APPLICATION_LAYER).mayOnlyBeAccessedByLayers(ADAPTERS_LAYER)
            .whereLayer(DOMAIN_LAYER).mayOnlyBeAccessedByLayers(ADAPTERS_LAYER, APPLICATION_LAYER);
```



### Project structure

Project structure slightly differes from the architecture. The main difference is in adapters tests - instead of grouping them by their purpose (inbound/outbound), I've decided to separate the contract tests from other integration tests, due to different technologies used (`pact` vs `test-containers`).

```bash
├── src
│   ├── architecture-test
│   ├── contract-test
│   ├── integration-test
│   ├── main
│   ├── test
│   └── use-case-test
```