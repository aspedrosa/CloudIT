package tqs.cloudit.services;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import tqs.cloudit.domain.rest.User;

public class StepsDefs extends TestApplication{
    
    private static WebDriver driver;

    public StepsDefs() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
    }
    
    @Autowired
    private AuthenticationService authenticationService;
    
    /*PROFILE TEST*/
    
    @Given("that I am logged in")
    public void openWebsiteAndLogin() {
        driver.get("http://localhost:8080/loginPage");
        
        System.out.println(authenticationService.register(new User("teste", "teste", "Teste", "teste@mail.com", "Freelancer", new ArrayList<>())));
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys("teste");
        pwd.sendKeys("teste");
        driver.findElement(By.id("submit_button")).click();
    }
    
    @When("I'm on the profile page")
    public void enterProfilePage() {
        driver.findElement(By.id("profile")).click();
        assertTrue(driver.findElements(By.id("profile_form")).size() > 0);
        assertEquals(driver.findElement(By.id("profile_form_title")).getText(), "Profile");
    }
    
    @Then("I should see the information that is visible to all members")
    public void seeProfileInfo() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) ->   d.findElement(By.id("username")).getText().equals("teste")
                                && d.findElement(By.id("type")).getAttribute("value").equals("Freelancer") 
                                && d.findElement(By.id("name")).getText().equals("Teste")
                                && d.findElement(By.id("email")).getText().equals("teste@mail.com"));
    }
    
    @Then("be able to update any information I desire")
    public void clickUpdateInfo() {
        assertTrue(driver.findElements(By.id("update_btn")).size() > 0);
    }
    
    @When("I submit any change to the information") 
    public void submitProfileUpdate() {
        WebElement name = driver.findElement(By.id("name"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement cur_pwd = driver.findElement(By.id("cur_pwd"));
        WebElement new_pwd = driver.findElement(By.id("new_pwd"));
        WebElement new_pwd_conf = driver.findElement(By.id("new_pwd_conf"));
        name.sendKeys("Teste 2");
        email.sendKeys("teste2@mail.com");
        cur_pwd.sendKeys("teste");
        new_pwd.sendKeys("teste2");
        new_pwd_conf.sendKeys("teste2");
        driver.findElement(By.id("update_btn")).click();
    }
    
    @Then("I should see all changes instantly")
    public void seeProfileInfoUpdated() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) ->   d.findElement(By.id("name")).getText().equals("Teste 2")
                                && d.findElement(By.id("email")).getText().equals("teste2@mail.com"));
    }
    
    @When("I submit any change to the information without the correct format") 
    public void submitProfileUpdateIncorrect() {
        WebElement name = driver.findElement(By.id("name"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement cur_pwd = driver.findElement(By.id("cur_pwd"));
        WebElement new_pwd = driver.findElement(By.id("new_pwd"));
        WebElement new_pwd_conf = driver.findElement(By.id("new_pwd_conf"));
        name.sendKeys("Teste 2");
        email.sendKeys("teste2@mail.com");
        cur_pwd.sendKeys("pass errada");
        new_pwd.sendKeys("teste2");
        new_pwd_conf.sendKeys("teste2");
        driver.findElement(By.id("update_btn")).click();
    }
    
    @Then("I should be notified about the errors or missing fields")
    public void notifyIncorrectFields() {
        
    }
    
    @Then("have the chance to correct my submission")
    public void correctSubmission() {
        
    }
    
    /*SIGNIN TEST*/
    
    @Given("that I have access to the platform's website,")
    public void openWebsite() {
        driver.get("http://localhost:8080/loginPage");
    }
    
    @When("I login")
    public void Login() throws InterruptedException {
        System.out.println(authenticationService.register(new User("teste", "teste", "", "", "Freelancer", new ArrayList<>())));
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys("teste");
        pwd.sendKeys("teste");
        driver.findElement(By.id("submit_button")).click();
    }
    
    /*
    @Then("I should see the welcome page.")
    public void checkWelcomePage() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.id("welcome_title")).getText().toLowerCase().equals("welcome teste!"));
        driver.quit();
    }
    */
    
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
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElements(By.id("invalid_credentials_message")).size() > 0);
        assertTrue(driver.findElements( By.id("invalid_credentials_message") ).size() > 0);
        assertEquals(driver.findElement( By.id("invalid_credentials_message") ).getText(), "Error! Invalid Credentials.");
        driver.quit();
    }
    
    /*SIGNUP TEST*/
    
    @When("I register in the platform")
    public void register() {
        driver.findElement(By.id("sign_up_link")).click();
        WebElement name = driver.findElement(By.id("name"));
        WebElement username = driver.findElement(By.id("username"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement type = driver.findElement(By.id("type"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        name.sendKeys("u1");
        username.sendKeys("u1");
        email.sendKeys("u1@mail.pt");
        type.sendKeys("Freelancer");
        pwd.sendKeys("123");
    }
    
    @And("I click the submit button")
    public void click(){
        driver.findElement(By.id("signUpButton")).click();
    }
    
    
    @Then("I should see the welcome page.")
    public void checkWelcomePage() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.id("welcome_title")).getText().toLowerCase().contains("welcome"));
        driver.quit();
    }
    
    
    @When("I register in the platform incorrectly")
    public void badRegister() {
        /*
        Not sending password.
        */
        driver.findElement(By.id("sign_up_link")).click();
        WebElement name = driver.findElement(By.id("name"));
        WebElement username = driver.findElement(By.id("username"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement type = driver.findElement(By.id("type"));
        name.sendKeys("teste");
        username.sendKeys("teste");
        email.sendKeys("teste@mail.pt");
        type.sendKeys("Freelancer");
    }
    
    @Then("I should be notified about the errors or missing fields")
    public void checkErrorRegisterMessage() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> { 
                                        try 
                                        { 
                                            driver.switchTo().alert(); 
                                            driver.switchTo().alert().accept();
                                            return true; 
                                        }   // try 
                                        catch (NoAlertPresentException Ex) 
                                        { 
                                            return false; 
                                        }   // catch 
                                    });
    }
    
    @And("have the chance to correct my submission.")
    public void canCorrectForm(){
        assertTrue(driver.findElements(By.id("name")).size() > 0);
        assertTrue(driver.findElements(By.id("username")).size() > 0);
        assertTrue(driver.findElements(By.id("email")).size() > 0);
        assertTrue(driver.findElements(By.id("pwd")).size() > 0);
        assertTrue(driver.findElements(By.id("type")).size() > 0);
        driver.quit();
    }
}
