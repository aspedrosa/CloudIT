package tqs.cloudit.services;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import java.util.TreeSet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import tqs.cloudit.domain.rest.User;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

@Ignore
public class AuthenticationStepsDefs extends TestApplication {
    
    private final WebDriver driver;
    
    @Autowired
    private AuthenticationService authenticationService;
   
    public AuthenticationStepsDefs() {
        WebDriverManager.phantomjs().setup();
        driver = new PhantomJSDriver();
    }
    
    @Given("that I have access to the platform's website,")
    public void openWebsite() {
        authenticationService.register(new User("teste", "teste", "", "", "", new TreeSet<>()));
        driver.get("http://localhost:8080/home");
    }
    
    @When("I login")
    public void Login() {
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys("teste");
        pwd.sendKeys("teste");
        driver.findElement(By.id("submit_button")).click();
    }
    
    @Then("I should see the welcome page.")
    public void checkWelcomePage() {
        new WebDriverWait(driver,10L).until(
                (WebDriver d) -> d.findElement(By.id("welcome_title")).getText().toLowerCase().equals("welcome teste!"));
        driver.quit();
    }
    
    @When("I login without filling the form correctly")
    public void BadLogin() {
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys("NonExistingAccount");
        pwd.sendKeys("NonExistingAccount");
        driver.findElement(By.id("submit_button")).click();
    }
    
    @Then("I should be notified about the errors or missing fields.")
    public void checkErroMessage() {
        new WebDriverWait(driver,10L).until(
                (WebDriver d) -> d.findElements(By.id("invalid_credentials_message")).size() > 0);
        assertTrue(driver.findElements( By.id("invalid_credentials_message") ).size() > 0);
        assertEquals(driver.findElement( By.id("invalid_credentials_message") ).getText(), "Error! Invalid Credentials.");
        driver.quit();
    }
}
