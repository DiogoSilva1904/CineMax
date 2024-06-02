package deti.tqs.cinemax.frontend.createmovieandsession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;

public class CreatemovieandsessionSteps {
    private WebDriver driver;
    private Wait<WebDriver> wait;
    private String createdUsername;

    @Given("the user is on the log in page")
    public void theUserIsOnTheLogInPage() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        driver.get("http://localhost:4200");
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

    @When("the user clicks on add movie button")
    public void theUserSelectsTheFirstFilm() {
        // Ensure the page has loaded and elements are present
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addmovie")));
        WebElement firstBuyTicketButton = driver.findElement(By.id("addmovie"));
        firstBuyTicketButton.click();
    }

    @And("the user fills the form with title {}, duration {}, studio {} and choose the first genre from the dropdown")
    public void theUserFillsTheForm(String title, String duration, String studio) {
        WebElement titleInput = driver.findElement(By.id("title"));
        WebElement durationInput = driver.findElement(By.id("duration"));
        WebElement studioInput = driver.findElement(By.id("studio"));
        WebElement genreDropdown = driver.findElement(By.id("genre"));
        WebElement createButton = driver.findElement(By.id("create"));

        titleInput.sendKeys(title);
        durationInput.sendKeys(duration);
        studioInput.sendKeys(studio);
        Select genreSelect = new Select(genreDropdown);
        genreSelect.selectByIndex(1);
        createButton.click();

    }

    @Then("the user should see the movie with title {} in the list")
    public void theUserShouldSeeTheMovie(String title) {
        //reload the page
        driver.get("http://localhost:4200/movies");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-movies/div[2]/div/div/div[1]/app-movie-card/div/section/header")));
        List<WebElement> movieTitles = driver.findElements(By.tagName("header"));
        boolean found = false;
        for (WebElement movieTitle : movieTitles) {
            if (movieTitle.getText().equals(title)) {
                found = true;
                break;
            }
        }
        assertEquals(true, found);
    }

    @Then("the user goes to the sessions page")
    public void theUserGoesToTheSessionsPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sessions")));
        WebElement sessionsButton = driver.findElement(By.id("sessions"));
        sessionsButton.click();
    }

    @When("the user clicks on add session button")
    public void theUserClicksOnAddSessionButton() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addsession")));
        WebElement addSessionButton = driver.findElement(By.id("addsession"));
        addSessionButton.click();
    }

    @And("the user fills the form with date {}, time {}, choose the first room from the dropdown and the movie with title {} from the dropdown")
    public void theUserFillsTheSessionForm(String date, String time, String title) {
        WebElement dateInput = driver.findElement(By.id("date"));
        WebElement timeInput = driver.findElement(By.id("time"));
        WebElement roomDropdown = driver.findElement(By.id("room"));
        WebElement movieDropdown = driver.findElement(By.id("movie"));
        WebElement createButton = driver.findElement(By.id("create"));

        dateInput.sendKeys(date);
        timeInput.sendKeys(time);
        Select roomSelect = new Select(roomDropdown);
        roomSelect.selectByIndex(1);
        Select movieSelect = new Select(movieDropdown);
        movieSelect.selectByVisibleText(title);
        createButton.click();
    }

    @Then("the user logs out")
    public void theUserLogsOut() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout")));
        WebElement logoutButton = driver.findElement(By.id("logout"));
        logoutButton.click();
    }

      



    @After
    public void cleanUp() {
        // Perform login to get the token
        // Clean up created user from the database

        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }


}
