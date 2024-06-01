package deti.tqs.cinemax.frontend.buyticket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.hamcrest.CoreMatchers.containsString;

public class BuyTicketSteps {
    private WebDriver driver;
    private Wait<WebDriver> wait;
    private String createdUsername;

    @Given("the user is on the log in page")
    public void theUserIsOnTheLogInPage() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:4200");
    }

    @When("the user does not have an account, so clicks on the button to create an account")
    public void theUserDoesNotHaveAnAccount() {
        WebElement createAccountButton = driver.findElement(By.className("create-account-btn"));
        createAccountButton.click();
    }

    @Then("the user creates an account with username {}, password {} and email {}")
    public void theUserCreatesAnAccount(String username, String password, String email) {
        createdUsername = username;
        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement confirmPasswordInput = driver.findElement(By.id("confirmPassword"));
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement createAccountButton = driver.findElement(By.id("register"));
        WebElement loginButton = driver.findElement(By.className("back-to-login-btn"));

        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        confirmPasswordInput.sendKeys(password);
        emailInput.sendKeys(email);
        createAccountButton.click();
        loginButton.click();

    }

    @Then("the user logs in with username {} and password {}")
    public void theUserLogsIn(String username, String password) {
        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login"));
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    @When("the user is in the main page")
    public void theUserIsInTheMainPage() {
        //WebElement homeButton = driver.findElement(By.className("home-btn"));
        //homeButton.click();
    }

    @After
    public void cleanUp() {
        // Perform login to get the token
        String token = loginAndGetToken("admin", "admin"); // Replace with admin credentials

        // Clean up created user from the database
        deleteUser(createdUsername, token);

        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }

    private String loginAndGetToken(String username, String password) {
        try {
            String loginUrl = "http://localhost:8080/api/login";
            HttpClient client = HttpClient.newHttpClient();
            String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(loginUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parse the JSON response to extract the JWT token
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.body());
                return rootNode.path("jwt").asText();
            } else {
                System.out.println("Failed to login: " + response.body());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void deleteUser(String username, String token) {
        try {
            String url = "http://localhost:8080/api/users/" + username;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            if (response.statusCode() != 200) {
                System.out.println("Failed to delete user: " + response.body());
            } else {
                System.out.println("User deleted successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
