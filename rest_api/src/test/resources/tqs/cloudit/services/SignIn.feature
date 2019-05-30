Feature: User can sign in on the platform.
    As a person interested in taking advantage of CloudIT's features.
    I want to be able to login to my account.
    So that I can start searching for opportunities in the application field.

    Scenario: User sign in on the platform.
        Given that I have access to the platform's website,
        When I login
        Then I should see the welcome page.

    Scenario: User fails sign in.
        Given that I have access to the platform's website,
        When I login without filling the form correctly
        Then I should be notified about the errors or missing fields.