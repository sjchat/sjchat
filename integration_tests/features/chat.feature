Feature: It should be possible to chat

  Background:
    Given user "user1" is created and logged in
    And user "user2" is created and logged in
    And a chat "testChat" is created
    #And a websocket session is started for user "user1"
    #And a websocket session is started for user "user2"

  Scenario: When connecting a websocket it tells you to enroll
    When user "user1" sends message "Hello, I am test message" to chat "testChat"
    #Given user "user3" is created and logged in
    #When a websocket session is started for user "user3" without enrolling
    #Then there is a message from the websocket for user "user3" that says: "Please enroll your userid with path 'enroll'"

  Scenario: I can send messages
    #When I send a message "Hello I am user1" from user "user2" to user "user2"
    #Then there is a message from the websocket for user "user2" that says: "Hello I am user1"

  Scenario: I can send messages an third parties can't see them
    #Given user "user3" is created and logged in
    #And a websocket session is started for user "user3"
    #When I send a message "Hello I am user1" from user "user2" to user "user2"
    #Then there is a message from the websocket for user "user2" that says: "Hello I am user1"
    #Then there is no message from the websocket for user "user1" that says: "Hello I am user1"
    #And there is no message from the websocket for user "user3" that says: "Hello I am user1"
