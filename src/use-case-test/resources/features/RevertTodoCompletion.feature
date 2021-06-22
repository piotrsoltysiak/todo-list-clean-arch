Feature: Revert Todo completion

  Scenario: Happy path
    Given I created todo list "Clean arch tech talk"
    And I created todo item "prepare the presentation" on list "Clean arch tech talk"
    And I complete the item "prepare the presentation" from list "Clean arch tech talk"
    When I revert the completion of item "prepare the presentation" from list "Clean arch tech talk"
    Then The item "prepare the presentation" is on the pending section of list "Clean arch tech talk"
    And The item "prepare the presentation" is not on the completed section of list "Clean arch tech talk"