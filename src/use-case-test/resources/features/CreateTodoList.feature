Feature: Create todo list

  Scenario: Happy path
    When I created todo list "Clean arch tech talk"
    Then The todo list "Clean arch tech talk" has no todo items
    Then The todo list "Clean arch tech talk" has no completed items