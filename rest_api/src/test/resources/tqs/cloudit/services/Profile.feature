Feature: Member can access the profile page and change its information
    As a logged in member of CloudIT
    I want to be able to view my personal information
    And update its fields any time
    And view other members' public information
    So that I can keep up-to-date
    And I can decide if it is an interesting person to contact.

    Scenario: User sees own profile information.
        Given that I am logged in,
        When I'm on the profile page
        Then I should see the information that is visible to all members.
        And be able to update any information I desire.
    Scenario: User sees other user profile information.
        Given that I am logged in,
        When I'm on another user profile page
        Then I should see the information he/she made available
        And be able to start a conversation.
    Scenario: User changes profile information.
        Given that I am logged in,
        When I'm on the profile page
        And I submit any change to the information
        Then I should see all changes instantly.
    Scenario: User tries to change profile information with errors.
        Given that I am logged in,
        When I'm on the profile page
        And I submit any change to the information without the correct format
        Then I should be notified about the errors or missing fields
        And have the chance to correct my submission.
