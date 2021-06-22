Feature: Remove todo list

  Scenario: Happy path
    Given I created todo list "Clean arch tech talk"
    And I created todo item "prepare the presentation" on list "Clean arch tech talk"
    When I removed todo list "Clean arch tech talk"
    Then Todo list "Clean arch tech talk" does not exist
