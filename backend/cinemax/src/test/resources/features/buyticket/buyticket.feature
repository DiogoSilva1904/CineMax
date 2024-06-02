@buy_ticket
Feature: Buy ticket
    Scenario:User creates an account, logs in, chooses a film and buys a ticket for a specific film
        Given the user is on the log in page
        When the user does not have an account, so clicks on the button to create an account
        Then the user creates an account with username rafa5481, password 123456 and email rafa548@gmail.com
        Then the user logs in with username rafa5481 and password 123456
        When the user selects the first film
        And chooses the first session
        And selects the third seat
        And clicks on reserve button
        Then the user should see a success message
        Then the user go to the tickets page
        Then the user should see the ticket
