Feature: User can search proposals in platform
    As an authenticated freelancer looking for a job opportunity
    I want to easily use the platform's search tab to find a filtered list of job proposals
    So that I can contact employers that I find appropriate for me.

    Scenario: Normal search
        Given that I am logged in,
        When I access the search tab
        And choose the option of job proposals for freelancers
        And I type in one or more keywords like the name of a programming language
        Then I should see a list job proposals related to that keyword.
    Scenario: Search with filters
        Given that I am logged in,
        When I access the search tab
        And choose the option of job proposals for freelancers
        And I type in one or more keywords like the name of a programming language
        And I choose a filtering option
        Then I should see a list of job proposals filtered according to the chosen rule.
    Scenario: Access Job information
        Given that I am logged in,
        When I access the search tab
        And choose the option of job proposals for freelancers
        And I type in one or more keywords like the name of a programming language
        And the results of the search are presented
        And I click on one job
        Then I should see all information related to that job
        And I should be able to contact the proposal's author.