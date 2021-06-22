Feature: Complete Todo

  Scenario: Happy path
    Given I created todo list "Clean arch tech talk"
    And I created todo item "prepare the presentation" on list "Clean arch tech talk"
    When I complete the item "prepare the presentation" from list "Clean arch tech talk"
    Then The item "prepare the presentation" is on the completed section of list "Clean arch tech talk"
    And The item "prepare the presentation" is not on the pending section of list "Clean arch tech talk"

  Scenario: Todo not found
    Given I created todo list "Clean arch tech talk"
    And I created todo item "prepare the presentation" on list "Clean arch tech talk"
    When I complete the item "complete the presentation" from list "Clean arch tech talk"
    Then Todo item not found error occurs