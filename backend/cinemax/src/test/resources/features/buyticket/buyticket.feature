@buy_ticket
Feature: Buy ticket
    Scenario:User creates an account, logs in, chooses a film and buys a ticket for a specific film
        Given the user is on the log in page
        When the user does not have an account, so clicks on the button to create an account
        Then the user creates an account with username rafa548, password 123456 and email rafa548@gmail.com
        Then the user logs in with username rafa548 and password 123456
        When the user selects the film The Godfather
        #And chooses the session
        #Then the user buys a ticket for the film "The Godfather"