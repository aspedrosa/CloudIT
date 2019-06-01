Feature: As an authenticated member with posts published
        I want to be free to edit or remove one or more posts of my profile
        So that my goals and desires are up to date.

    Scenario: 
        Given that I am logged in,
        And I have access to MyJobs page,
        And I have one or more posts published,
        When I choose the option to edit a job
        Then I should see a form prefilled with the current data.

    Scenario: 
        Given that I am logged in,
        When I execute the previous edit steps
        And I edit and submit the form,
        Then I should see a message informing me about the success/failure of the operation
        And (if successful) I should see the updates on my posts.
