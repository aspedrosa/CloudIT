Feature: Freelancer can post a personalized job proposal
    As an authenticated freelancer looking to work on a specific area / position / task / project
    I want to be free to post one or more job advertisements on the platform
    So that I can become more visible for employers potentially interested in me.

    Scenario: Freelancer asserts that it's possible to create a job offer.
        Given that I am logged in,
        And I have access to MyJobs page,
        When I choose the option to post a job advertisement
        Then I should see a form to be filled.

    Scenario: Freelancer creates a job offer, both correctly and without necessary fields.
        Given that I am logged in,
        When I execute the previous steps
        And I fill in and submit the form,
        Then I should see a message informing me about the success/failure of the operation
        And (if successful) I should see a new post added to my profile.