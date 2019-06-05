Feature: Employers can post a personalized job proposal
    As an authenticated employer searching to fill a temporary position for a specific task / project on a given area
    I want to be free to post one or more job offers on the platform
    So that potentially fit professionals can find me and contact me more easily.

    Scenario: Employer asserts that it's possible to create a job offer.
        Given that I am logged in,
        And I have access to MyJobs page,
        When I choose the option to post a job offer
        Then I should see a form to be filled.

    Scenario: Employer creates a job offer, both correctly and without necessary fields.
        Given that I am logged in,
        When I execute the previous steps
        And I fill in and submit the form,
        Then I should see a message informing me about the success/failure of the operation
        And (if successful) I should see a new post added to my profile.

