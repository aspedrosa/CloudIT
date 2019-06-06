Feature: Member can use the search tab to find other members
    As a logged in member of CloudIT
    I want to easily use the platform's search tab
    And find a filtered list of freelancers and/or employers affiliated to the platform
    So that I can contact those that I find appropriate for me.

    Scenario: Normal Search
        Given that I am logged in,
        When I access the search tab
        And choose the option of search for freelancers or employers
        And I click the search button
        Then I should see a list freelancers or employers

    Scenario: Search with filters
        Given that I am logged in,
        When I access the search tab
        And choose the option of search for freelancers or employers
        And I type in one or more keywords like the name of a technology field
        And I click the search button
        Then I should see a list freelancers or employers related to that/those keyword(s).

    Scenario: Access user information
        Given that I am logged in,
        When I access the search tab
        And choose the option of search for freelancers or employers
        And I click the search button
        And the results are presented
        And I click on member
        Then I should see all information related to him/her including possible job offers/proposals
        And I should be able to contact the member.
