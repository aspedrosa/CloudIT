package tqs.cloudit.services;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.TreeSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import tqs.cloudit.domain.rest.User;

/**
 * Where all the steps of all features are defined here
 * Each step has referenced on javadoc on what Scenario(s) of which Feature(s) it is used
 */
public class StepsDefs extends TestApplication{

    private WebDriver driver;

    /**
     * Used to store the username of the last registered user
     * Login steps use this for the username field
     * Variable is static because Class is instantiated for each feature
     */
    private static String currentUsername;

    /**
     * Used to create a new current username making
     *  it different from the last one by incrementing
     *  the last one
     * Variable is static because Class is instantiated for each feature
     */
    private static int usernameCount = 0;

    /**
     * Creates a new username different from the previous
     *  one used so "username already in use" problems
     *  don't exist
     */
    private void changeCurrentUsername() {
        usernameCount++;
        currentUsername = "cucumber_tests" + usernameCount;
    }

    public StepsDefs() {
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);

        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver(options);
    }

    @Autowired
    private AuthenticationService authenticationService;

    /*
     * Specific to feature -> User can sign up on the platform (SignUp.feature)
     */

    /**
     * User can sign up on the platform (SignUp.feature)
     *  - User register in on the platform.
     *  - User fails to register in the platform.
     *
     * User can sign in on the platform. (SignIn.feature)
     *  - User sign in on the platform.
     *  - User fails sign in.
     *
     *  User can learn about the use of the platform through the website. (AboutPlatform.feature)
     *  - User accesses the platform's about page.
     *  - User logs in and learns more about the platform.
     */
    @Given("that I have access to the platform's website,")
    public void openWebsite() {
        driver.get("http://localhost:8080/loginPage");
    }

    /**
     * User can sign in on the platform. (SignIn.feature)
     *  - User sign in on the platform.
     *
     *  User can learn about the use of the platform through the website. (AboutPlatform.feature)
     *  - User logs in and learns more about the platform.
     */
    @When("I login")
    public void Login() throws InterruptedException {
        changeCurrentUsername();

        User newUser = new User(
            currentUsername,
            "test",
            "",
            currentUsername + "@mail.com",
            "",
            new ArrayList<>()
        );
        System.out.println(authenticationService.register(newUser));

        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys(currentUsername);
        pwd.sendKeys("test");
        driver.findElement(By.id("submit_button")).click();
    }

    /**
     * User can sign in on the platform. (SignIn.feature)
     *  - User fails sign in.
     */
    @When("I login without filling the form correctly")
    public void BadLogin() {
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys("NonExistingAccount");
        pwd.sendKeys("NonExistingAccount");
        driver.findElement(By.id("submit_button")).click();
    }

    /**
     * User can sign up on the platform (SignUp.feature)
     *  - User fails to register in the platform.
     *
     * User can sign in on the platform. (SignIn.feature)
     *  - User fails sign in.
     */
    @Then("I should be notified about the errors or missing fields.")
    public void checkErrorMessage() {
        new WebDriverWait(driver,10L).until(
                (WebDriver d) -> d.findElements(By.id("invalid_credentials_message")).size() > 0);
        assertTrue(driver.findElements( By.id("invalid_credentials_message") ).size() > 0);
        assertEquals(driver.findElement( By.id("invalid_credentials_message") ).getText(), "Error! Invalid Credentials.");
        driver.quit();
    }

    /*
     * Specific to feature -> User can sign in on the platform. (SignIn.feature)
     */

    /**
     * User can sign up on the platform (SignUp.feature)
     *  - User register in on the platform.
     */
    @When("I register in the platform")
    public void register() {
        changeCurrentUsername();

        driver.findElement(By.id("sign_up_link")).click();
        WebElement name = driver.findElement(By.id("name"));
        WebElement username = driver.findElement(By.id("username"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement type = driver.findElement(By.id("type"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        name.sendKeys("test");
        username.sendKeys(currentUsername);
        email.sendKeys(currentUsername + "@mail.pt");
        type.sendKeys("freelancer");
        pwd.sendKeys("test");
    }

    /**
     * User can sign up on the platform (SignUp.feature)
     *  - User register in on the platform.
     *  - User fails to register in the platform.
     */
    @And("I click the submit button")
    public void click(){
        driver.findElement(By.id("signUpButton")).click();
    }

    /**
     * User can sign up on the platform (SignUp.feature)
     *  - User register in on the platform.
     *
     * User can sign in on the platform. (SignIn.feature)
     *  - User sign in on the platform.
     */
    @Then("I should see the welcome page.")
    public void checkWelcomePage() {
        new WebDriverWait(driver,10L).until(
                (WebDriver d) ->  {
                    WebElement welcome_title = driver.findElement(By.id("welcome_title"));
                    if (welcome_title == null)
                        return false;
                    return welcome_title.getText().toLowerCase().equals(String.format("welcome %s!", currentUsername));
                });
        driver.quit();
    }

    /**
     * User can sign up on the platform (SignUp.feature)
     *  - User fails to register in the platform.
     */
    @When("I register in the platform incorrectly")
    public void badRegister() {
        /*
        Not sending password.
        */
        changeCurrentUsername();

        driver.findElement(By.id("sign_up_link")).click();
        WebElement name = driver.findElement(By.id("name"));
        WebElement username = driver.findElement(By.id("username"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement type = driver.findElement(By.id("type"));
        name.sendKeys("test");
        username.sendKeys(currentUsername);
        email.sendKeys(currentUsername + "@mail.pt");
        type.sendKeys("freelancer");
    }

    /**
     * User can sign up on the platform (SignUp.feature)
     *  - User fails to register in the platform.
     *
     * User can sign in on the platform. (SignIn.feature)
     *  - User fails sign in.
     */
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

    /**
     * User can sign up on the platform (SignUp.feature)
     *  - User fails to register in the platform.
     */
    @And("have the chance to correct my submission.")
    public void canCorrectForm(){
        assertTrue(driver.findElements(By.id("name")).size() > 0);
        assertTrue(driver.findElements(By.id("username")).size() > 0);
        assertTrue(driver.findElements(By.id("email")).size() > 0);
        assertTrue(driver.findElements(By.id("pwd")).size() > 0);
        assertTrue(driver.findElements(By.id("type")).size() > 0);
        driver.quit();
    }
    

    /*
     * Specific to feature -> User can learn about the use of the platform through the website. (AboutPlatform.feature)
     */

    /**
     *  User can learn about the use of the platform through the website. (AboutPlatform.feature)
     *  - User accesses the platform's about page.
     *  - User logs in and learns more about the platform.
     */
    @When("I click on the about tab")
    public void clickAbout() {
        driver.findElement(By.id("about")).click();
    }

    /**
     *  User can learn about the use of the platform through the website. (AboutPlatform.feature)
     *  - User accesses the platform's about page.
     *  - User logs in and learns more about the platform.
     */
    @Then("I should see the about page.")
    public void checkAboutPage() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> d.findElement(By.id("about_page_content")) != null);
        driver.quit();
    }

    
    /*
     * Specific to feature -> Employers can post a personalized job proposal. (EmployerPostJob.feature)
     */

    /**
     * Employers can post a personalized job proposal. (EmployerPostJob.feature)
     * - Employer asserts that it's possible to create a job offer.
     * - Employer creates a job offer, both correctly and without necessary fields.
     */
    @Given("that I am logged in,")
    public void loggedIn() throws InterruptedException {
        driver.get("http://localhost:8080/loginPage");
        System.out.println(authenticationService.register(new User("teste", "teste", "", "", "Freelancer", new ArrayList<>())));
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys("teste");
        pwd.sendKeys("teste");
        driver.findElement(By.id("submit_button")).click();
    }

    /**
     * Employers can post a personalized job proposal. (EmployerPostJob.feature)
     * - Employer asserts that it's possible to create a job offer.
     */
    @Given("I have accessed to MyJobs page,")
    public void accessedMyJobs() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.id("jobs")).isDisplayed());
        driver.findElement(By.id("jobs")).click();
    }

    /**
     * Employers can post a personalized job proposal. (EmployerPostJob.feature)
     * - Employer asserts that it's possible to create a job offer.
     */
    @When("I choose the option to post a job offer")
    public void chooseOptionPostOffer() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.id("createJobButton")).isDisplayed());
        driver.findElement(By.id("createJobButton")).click();
    }

    /**
     * Employers can post a personalized job proposal. (EmployerPostJob.feature)
     * - Employer asserts that it's possible to create a job offer.
     */
    @Then("I should see a form to be filled.")
    public void shouldSeeForm() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.id("offerTitle")).isDisplayed());
        assertTrue(driver.findElement(By.id("offerTitle")).isDisplayed());
        assertTrue(driver.findElement(By.id("offerDescription")).isDisplayed());
        assertTrue(driver.findElement(By.id("offerArea")).isDisplayed());
        assertTrue(driver.findElement(By.id("offerAmount")).isDisplayed());
        assertTrue(driver.findElement(By.id("offerDate")).isDisplayed());
        driver.quit();
    }

    /**
     * Employers can post a personalized job proposal. (EmployerPostJob.feature)
     * - Employer creates a job offer, both correctly and without necessary fields.
     */
    @When("I execute the previous steps")
    public void executePreviousSteps() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.id("jobs")).isDisplayed());
        driver.findElement(By.id("jobs")).click();
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.id("createJobButton")).isDisplayed());
        driver.findElement(By.id("createJobButton")).click();
    }

    /**
     * Employers can post a personalized job proposal. (EmployerPostJob.feature)
     * - Employer creates a job offer, both correctly and without necessary fields.
     */
    @When("I fill in and submit the form,")
    public void fillAndSubmit() {
        WebElement title = driver.findElement(By.id("offerTitle"));
        WebElement description = driver.findElement(By.id("offerDescription"));
        WebElement area = driver.findElement(By.id("offerArea"));
        WebElement amount = driver.findElement(By.id("offerAmount"));
        WebElement date = driver.findElement(By.id("offerDate"));
        title.sendKeys("t1");
        description.sendKeys("t1");
        area.sendKeys("t1");
        amount.sendKeys("1");
        date.sendKeys("1010-11-11");
        driver.findElement(By.id("submitOfferButton")).click();
    }

    /**
     * Employers can post a personalized job proposal. (EmployerPostJob.feature)
     * - Employer creates a job offer, both correctly and without necessary fields.
     */
    @Then("I should see a message informing me about the success\\/failure of the operation")
    public void shouldSeeMessageInformingSuccessFailure() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> !d.findElement(By.id("offerTitle")).isDisplayed());
        assertFalse(driver.findElement(By.id("offerTitle")).isDisplayed());
        assertFalse(driver.findElement(By.id("offerDescription")).isDisplayed());
        assertFalse(driver.findElement(By.id("offerArea")).isDisplayed());
        assertFalse(driver.findElement(By.id("offerAmount")).isDisplayed());
        assertFalse(driver.findElement(By.id("offerDate")).isDisplayed());
    }

    /**
     * Employers can post a personalized job proposal. (EmployerPostJob.feature)
     * - Employer creates a job offer, both correctly and without necessary fields.
     */
    @Then("\\(if successful) I should see a new post added to my profile.")
    public void ifSuccessfulShouldSeeNewPostAddedToProfile() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.linkText("t1")).isDisplayed());
        driver.quit();
    }
    
    
}
