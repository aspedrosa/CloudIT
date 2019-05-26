package tqs.cloudit.services;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.TreeSet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import tqs.cloudit.domain.rest.User;

@Ignore
public class AboutPlatformStepsDefs extends TestApplication {
    private final WebDriver driver;
    
    @Autowired
    private AuthenticationService authenticationService;

    public AboutPlatformStepsDefs() {
        WebDriverManager.firefoxdriver().setup();
        //driver = new PhantomJSDriver();
        driver = new FirefoxDriver();
    }
    
    @Given("that I have access to the platform's website,")
    public void openWebsite() {
        authenticationService.register(new User("Test Username", "Test Password", "", "", "", new TreeSet<>()));
        driver.get("http://localhost:8080/home");
    }
    
    @When("I click on the about tab")
    public void clickAbout() {
        driver.findElement(By.id("about")).click();
    }
    
    @Then("I should see the about page.")
    public void checkAboutPage() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.id("about_page_content")) != null);
        driver.quit();
    }
    
    @When("I login")
    public void Login() {
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys("Test Username");
        pwd.sendKeys("Test Password");
        driver.findElement(By.id("submit_button")).click();
    }
}
