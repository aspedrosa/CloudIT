Feature: Users can show interest on posts through job posts
    As an authenticated employer or freelancer who found the most appropriate solution to fill a temporary position
    I want to be able to easily present myself as interested through an existing job post
    So that I can start working with its creator.

    Scenario: Freelancer shows interest in job through post
        Given that I am logged in,
        And I have accessed the intended Employer’s profile,
        And I click on his/her job offer,
        When I click on the contact button
        Then I should be redirected to the message center’s conversation with the Employer
        And I should see an automatic private message mentioning the interest.

    Scenario: Employer receives message about Freelancer's interest 
        Given that I am logged in,
        When I'm on the messaging center page,
        And a Freelancer has shown interest in one of my posts,
        And I click on the conversation with the Freelancer,
        Then I should see an automatic private message mentioning the interest.

