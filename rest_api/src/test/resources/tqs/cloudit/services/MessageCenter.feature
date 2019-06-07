Feature: Member can contact others through the platform's message center
    As a logged in member of CloudIT
    I want to be able to send/receive messages to/from other members
    And view all messages organized by conversations and sorted from latest to oldest
    So that I can make other actions with more information regarding entities envolved.

    Scenario: See existing conversations
        Given that I am logged in,
        When I'm on the messaging center page
        Then I should see the conversations I had with other members sorted by latest message
        And be able to click on one of the conversations.
    Scenario: Write message on conversation
        Given that I am logged in,
        When I'm on the messaging center page
        And I click on one of the conversations
        Then I should see the messages I traded with the other member sorted by latest message
        And be able to send him/her a new message.
