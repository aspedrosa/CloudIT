Feature: Employers can propose hiring through the message center
    As an authenticated employer who found the most appropriate professional to fill a temporary position
    I want to be able to easily propose to him/her one of my job offers through the message center
    So that I can start working with the freelancer.

    Scenario: Employer proposes existing job offer
        Given that I am logged in,
        And I have accessed the message center’s conversation with the freelancer
        When I select one of my job offers for him/her
        Then I should see an automatic private message mentioning the pending hiring offer.
    Scenario: Employer verifies answer from Freelancer
        Given that I am logged in,
        And I have accessed the message center’s conversation with the freelancer
        And he/she has responded to my request
        Then I should see the response.