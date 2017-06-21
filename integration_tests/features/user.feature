Feature: There should be users
  It should be possible to create users
  Without any special permissions

  Scenario: Create user
    When I create a user "user1" with password "user1"
    Then the response has status code 201
    And the response includes property "username" with value "user1"
    And the response includes property "realName" with value "user1"
    And the response includes property "id"
    And the response does NOT include property "password"

  Scenario: Create user that already exists
    Given I create a user "user1" with password "user1"
    When I create a user "user1" with password "pass"
    Then the response has status code 422
    And the response includes property "message" with value "User with that username already exists"

  Scenario Outline: Create user with not all required properties set
    When I create a user without property "<prop>"
    Then the response has status code 406
    And the response includes property "message" with value "Not all required properties set"

    Examples:
      | prop      |
      | username  |
      | password  |
      | realName  |

  Scenario: Login user
    Given I create a user "user1" with password "pass"
    When I log in user "user1" with password "pass"
    Then I have a token

  Scenario: Login user with wrong password
    Given I create a user "user1" with password "pass"
    When I log in user "user1" with password "pass2"
    Then I don't have a token

  Scenario: Login user with wrong username
    Given I create a user "user1" with password "pass"
    When I log in user "user2" with password "pass"
    Then I don't have a token
