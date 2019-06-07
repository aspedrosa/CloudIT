Feature: User can search offers in platform
         As an authenticated employer looking for a professional to execute a given task with a certain degree of complexity
         I want to easily use the platform's search tab to find a filtered list of job offers or of specific freelancers
         So that I can find the best person for the task.

    Scenario: Normal search
        Given that I am logged in,
        When I access the search tab
        And choose the option of job offers for employers
        And I type in one or more keywords like the name of a programming language
        Then I should see a list job offers related to that keyword.
    Scenario: Search with filters
        Given that I am logged in,
        When I access the search tab
        And choose the option of job offers for employers
        And I type in one or more keywords like the name of a programming language
        And I choose a filtering option
        Then I should see a list of job offers filtered according to the chosen rule.
    Scenario: Access Job information
        Given that I am logged in,
        When I access the search tab
        And choose the option of job offers for employers
        And I type in one or more keywords like the name of a programming language
        And the results of the offer search are presented
        And I click on one job
        Then I should see all information related to that job
        And I should be able to contact the proposal's author.
