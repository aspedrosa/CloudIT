Feature: User can sign up on the platform
    As a person interested in taking advantage of CloudIT's features.
    I want to be able to register in the platform.
    So that I can start searching for opportunities in the application field.

    Scenario: User register in on the platform.
        Given that I have access to the platform's website,
        When I register in the platform
        And I click the submit button
        Then I should see the welcome page.

    Scenario: User fails to register in the platform.
        Given that I have access to the platform's website,
        When I register in the platform incorrectly
        And I click the submit button
        Then I should be notified about the errors or missing fields
        And have the chance to correct my submission.