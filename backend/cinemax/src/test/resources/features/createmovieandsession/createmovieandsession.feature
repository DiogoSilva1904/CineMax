@createmovieandsession
Feature: Create movie and Session
    Scenario:User logs in, creates a film and adds a session
        Given the user is on the log in page
        Then the user logs in with username admin and password admin
        When the user clicks on add movie button
        And the user fills the form with title Drive, duration 128, studio Universal Studios and choose the first genre from the dropdown
        Then the user should see the movie with title Drive in the list
        Then the user goes to the sessions page
        When the user clicks on add session button
        And the user fills the form with date 2024-05-30, time 21:00, choose the first room from the dropdown and the movie with title Drive from the dropdown
        Then the user logs out