Feature: Employers can propose hiring through job posts
    As an authenticated employer who found the most appropriate professional to fill a temporary position
    I want to be able to easily present myself as interested in the freelancer through an existing job post
    So that I can start working with him/her.

    Scenario: Employer proposes job through post
        Given that I am logged in,
        And I have accessed the intended freelancer’s profile page,
        And I click on his/her job offer,
        When I click on the contact button
        Then I should be redirected to the message center’s conversation with the freelancer
        And I should see an automatic private message mentioning the interest in hiring him/her.

