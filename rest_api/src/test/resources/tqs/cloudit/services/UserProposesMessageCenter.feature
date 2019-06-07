Feature: User can propose hiring / getting hired through the message center
    As an authenticated user who found the most appropriate user to work with
    I want to be able to easily propose to the employer/freelancer one of our posts through the message center
    So that I can start working with him/her.

    Scenario: Freelancer proposes own offer to Employer
        Given that I am logged in,
        And I have accessed the message center’s conversation with the employer
        When I click on the plus button
        And I select one of our job proposals
        Then I should see an automatic private message mentioning the pending hiring proposal.
    Scenario: Employer answers hiring proposal
        Given that I am logged in,
        When I'm on the messaging center page
        And a Freelancer has sent a hiring proposal to me
        Then I should see his/her hiring proposal
        And be able to accept or deny it.
    Scenario: Freelancer verifies answer from Employer
        Given that I am logged in,
        And I have accessed the message center’s conversation with the employer
        And he/she has responded to my request
        Then I should see the response.
