Feature: Create todo list

  Scenario:
    When I created todo list "Clean arch tech talk"
    Then The todo list "Clean arch tech talk" has no pending items
    Then The todo list "Clean arch tech talk" has no completed items