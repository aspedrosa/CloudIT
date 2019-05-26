Feature: User can learn about the use of the platform through the website.
	As a person with no knowledge about the advantages of CloudIT's features
	I want to be able to access the platform and learn all I need about it
	So that I can decide wether it is appropriate for me or not.

	Scenario: User accesses the platform's about page.
        Given that I have access to the platform's website,
		When I click on the about tab
		Then I should see the about page.

	Scenario: User logs in and learns more about the platform.
		Given that I have access to the platform's website,
		When I login
		And I click on the about tab
		Then I should see the about page.