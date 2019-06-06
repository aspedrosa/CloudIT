Feature: Freelancer can propose getting hired through the message center
    As an authenticated freelancer who found the most appropriate employer/job offer to work with/in
    I want to be able to easily propose to the employer one of my job offers or show interest in one of his/her through the message center
    So that I can start working with the employer.

    Scenario: Freelancer proposes own offer to Employer
        Given that I am logged in,
        And I have accessed the intended employer's profile page
        When I click on the contact button
        And I select one of my job proposals for him/her
        Then I should see an automatic private message mentioning the pending hiring proposal.
    Scenario: Freelancer verifies answer from Employer
        Given that I am logged in,
        And I have accessed the message center’s conversation with the employer
        And he/she has responded to my request
        Then I should see the response.
    Scenario: Freelancer proposes Employer's offer
        Given that I am logged in,
        And I have accessed the intended employer's profile page
        When I click on one of his/her job offers,
        And I click on the contact button
        Then I should be redirected to the conversation with him/her
        And I should see an automatic private message mentioning my interest.