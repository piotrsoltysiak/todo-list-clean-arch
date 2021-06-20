Feature: Remove todo item

  Scenario:
    Given I created todo list "Clean arch tech talk"
    And I created todo item "prepare the presentation" on list "Clean arch tech talk"
    When I removed todo item "prepare the presentation" from list "Clean arch tech talk"
    Then The todo list "Clean arch tech talk" has no pending items
    Then The todo list "Clean arch tech talk" has no completed items
