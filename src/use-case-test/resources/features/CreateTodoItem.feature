Feature: Create todo item

  Scenario:
    When I created todo list "Clean arch tech talk"
    And I created todo item "prepare the presentation" on list "Clean arch tech talk"
    Then The item "prepare the presentation" is on the pending section of list "Clean arch tech talk"
    And The todo list "Clean arch tech talk" has no completed items