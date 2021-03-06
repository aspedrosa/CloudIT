package tqs.cloudit.services;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import tqs.cloudit.domain.persistance.Message;
import tqs.cloudit.domain.rest.Job;
import tqs.cloudit.domain.rest.User;

/**
 * Where all the steps of all features are defined here
 * Each step has referenced on javadoc on what Scenario(s) of which Feature(s) it is used
 */
@TestPropertySource (value={"classpath:application.properties"})
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StepsDefs {

    private long MAX_WAIT_TIME = 500;

    private static WebDriver driver;

    static {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--whitelisted-ips");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disabled-extensions");

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
    }

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

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JobService jobService;
    
    @Autowired
    private MessageService messageService;

    /* ============================== SIGNIN TEST ============================== */

    /**
     * SignUp -> all
     *
     * SignIn -> all
     *
     * AboutPlatform.feature -> all
     */
    @Given("that I have access to the platform's website,")
    public void openWebsite() {
        driver.get("http://localhost:8080/loginPage");
    }

    /**
     * SignIn -> 1
     *
     * AboutPlatform -> 2
     */
    @When("I login")
    public void Login() {
        changeCurrentUsername();

        User newUser = new User(
            currentUsername,
            "test",
            "",
            currentUsername + "@mail.com",
            "Freelancer",
            new ArrayList<>()
        );
        System.out.println(authenticationService.register(newUser));

        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys(currentUsername);
        pwd.clear();
        pwd.sendKeys("test");
        driver.findElement(By.id("submit_button")).click();
    }
    
    /**
     * SignUp -> 1
     *
     * SignIn -> 1
     */
    @Then("I should see the welcome page.")
    public void checkWelcomePage() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until(
                d ->  d.findElement(By.id("welcome_title")).getText().toLowerCase()
                        .equals(String.format("welcome %s!", currentUsername.toLowerCase()))
            );
    }

    /**
     * SignIn -> 2
     */
    @When("I login without filling the form correctly")
    public void BadLogin() {
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys("NonExistingAccount");
        pwd.clear();
        pwd.sendKeys("NonExistingAccount");
        driver.findElement(By.id("submit_button")).click();
    }

    /**
     * SignUp -> 2
     *
     * SignIn -> 2
     */
    @Then("I should be notified about the invalid fields.")
    public void checkErrorMessage() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until(
                (WebDriver d) -> d.findElements(By.id("invalid_credentials_message")).size() > 0
            );

        assertEquals("Error! Invalid Credentials.", driver.findElement( By.id("invalid_credentials_message") ).getText());
    }
    
    /* ============================== SIGNUP TEST ============================== */
    
    /*
    @Given("that I have access to the platform's website,") -> Sign In Steps
    */

    /**
     * SignUp.feature -> 1
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
        email.clear();
        email.sendKeys(currentUsername + "@mail.pt");
        type.sendKeys("Freelancer");
        pwd.clear();
        pwd.sendKeys("test");
    }

    /**
     * SignUp.feature -> all
     */
    @And("I click the submit button")
    public void click(){
        driver.findElement(By.id("signUpButton")).click();
    }

    /*
    @Then("I should see the welcome page.") -> Sign In Steps
    */

    /**
     * SignUp.feature -> 2
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
        email.clear();
        email.sendKeys(currentUsername + "@mail.pt");
        type.sendKeys("Freelancer");
    }
    
    /*
    @And("I click the submit button") -> Above this step
    */

    /**
     * SignUp -> 2
     *
     * SignIn -> 2
     */
    @Then("I should be notified about the errors or missing fields")
    public void checkErrorRegisterMessage() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> { 
                    try {
                        driver.switchTo().alert().accept();
                        return true;
                    }
                    catch (NoAlertPresentException Ex) {
                        return false;
                    }
                }
            );
    }
    
    /**
     * SignUp -> 2
     */
    @And("have the chance to correct my submission.")
    public void canCorrectForm(){
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .until(
                (WebDriver d) -> d.findElement(By.id("name")).isDisplayed()
            );

        assertFalse(driver.findElement(By.id("name")).getAttribute("value").isEmpty());
        assertFalse(driver.findElement(By.id("email")).getAttribute("value").isEmpty());
    }
    
    /* ============================== ABOUT TEST ============================== */
    
    /*
    @Given("that I have access to the platform's website,") -> Sign In Steps
    */
    
    /*
    @When("I login") -> Sign In Steps
    */

    /**
     *  AboutPlatform -> all
     */
    @When("I click on the about tab")
    public void clickAbout() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .until(driver -> {
                driver.findElement(By.id("about")).click();
                return true;
            });
    }

    /**
     *  AboutPlatform -> all
     */
    @Then("I should see the about page.")
    public void checkAboutPage() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until(driver ->
                driver.findElement(By.id("about_page_content")).isDisplayed()
            );
    }

    /* ============================== EMPLOYERPOSTJOB TEST ============================== */

    /**
     * EmployerPostJob -> all
     * Profile -> all
     * SearchUser -> all
     */
    @Given("that I am logged in,")
    public void openWebsiteAndLogin() {
        driver.get("http://localhost:8080/loginPage");
        
        changeCurrentUsername();

        User newUser = new User(
            currentUsername,
            "test",
            "",
            currentUsername + "@mail.com",
            "Freelancer",
            new ArrayList<>()
        );
        System.out.println(authenticationService.register(newUser));
        
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys(currentUsername);
        pwd.clear();
        pwd.sendKeys("test");
        driver.findElement(By.id("submit_button")).click();
    }
    
    /**
     * EmployerPostJob -> 1
     *
     * FreelancerPostJob -> 1
     */
    @Given("I have access to MyJobs page,")
    public void accessedMyJobs() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("jobs")).isDisplayed()
            );

        long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);

        driver.findElement(By.id("jobs")).click();
    }

    /**
     * EmployerPostJob -> 1
     *
     * FreelancerPostJob -> 1
     */
    @When("I choose the option to post a job (offer|advertisement)")
    public void chooseOptionPostOffer() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("createJobButton")).isDisplayed()
            );
        driver.findElement(By.id("createJobButton")).click();
    }

    /**
     * EmployerPostJob -> 1
     *
     * FreelancerPostJob -> 1
     */
    @Then("I should see a form to be filled.")
    public void shouldSeeForm() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("offerTitle")).isDisplayed()
            );

        assertTrue(driver.findElement(By.id("offerTitle")).isDisplayed());
        assertTrue(driver.findElement(By.id("offerDescription")).isDisplayed());
        assertTrue(driver.findElement(By.id("offerArea")).isDisplayed());
        assertTrue(driver.findElement(By.id("offerAmount")).isDisplayed());
        assertTrue(driver.findElement(By.id("offerDate")).isDisplayed());
    }

    /**
     * EmployerPostJob -> 2
     *
     * FreelancerPostJob -> 2
     */
    @When("I execute the previous steps")
    public void executePreviousSteps() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("jobs")).isDisplayed()
            );

        long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);

        driver.findElement(By.id("jobs")).click();

        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("createJobButton")).isDisplayed()
            );

        driver.findElement(By.id("createJobButton")).click();
    }

    /**
     * EmployerPostJob -> 2
     *
     * FreelancerPostJob -> 2
     */
    @When("I fill in and submit the form,")
    public void fillAndSubmit() {
        WebElement description = driver.findElement(By.id("offerDescription"));
        WebElement area = driver.findElement(By.id("offerArea"));
        WebElement amount = driver.findElement(By.id("offerAmount"));
        WebElement date = driver.findElement(By.id("offerDate"));

        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(ElementNotInteractableException.class)
            .until(
                d -> {
                    driver.findElement(By.id("offerTitle")).sendKeys("t1");
                    return true;
                }
            );

        description.sendKeys("t1");
        area.sendKeys("t1");
        amount.sendKeys("1");
        date.sendKeys("1010-11-11");

        long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
                (WebDriver d) -> System.currentTimeMillis()-then > 1000);

        driver.findElement(By.id("submitOfferButton")).click();
    }

    /**
     * EmployerPostJob -> 2
     *
     * FreelancerPostJob -> 2
     */
    @Then("I should see a message informing me about the success\\/failure of the operation")
    public void shouldSeeMessageInformingSuccessFailure() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> !d.findElement(By.id("offerTitle")).isDisplayed()
            );

        assertFalse(driver.findElement(By.id("offerTitle")).isDisplayed());
        assertFalse(driver.findElement(By.id("offerDescription")).isDisplayed());
        assertFalse(driver.findElement(By.id("offerArea")).isDisplayed());
        assertFalse(driver.findElement(By.id("offerAmount")).isDisplayed());
        assertFalse(driver.findElement(By.id("offerDate")).isDisplayed());
    }

    /**
     * EmployerPostJob -> 2
     *
     * FreelancerPostJob -> 2
     */
    @And("\\(if successful) I should see a new post added to my profile.")
    public void ifSuccessfulShouldSeeNewPostAddedToProfile() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.linkText("t1")).isDisplayed()
            );
    }
    
    /* ============================== PROFILE TEST ============================== */
    
    /*
    @Given("that I am logged in,") -> EmployerPostJob Steps
    */

    /**
     * Profile -> 1, 2, 4
     */
    @When("I'm on the profile page")
    public void enterProfilePage() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("profile")).isDisplayed()
            );

        long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);

        driver.findElement(By.id("profile")).click();

        assertTrue(driver.findElement(By.id("profile_form")).isDisplayed());
        assertEquals("Profile", driver.findElement(By.id("profile_form_title")).getText());
    }

    /**
     * Profile -> 1
     */
    @Then("I should see the information that is visible to all members.")
    public void seeProfileInfo() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) ->   d.findElement(By.id("username")).getText().equals(currentUsername)
                                && d.findElement(By.id("type")).getText().equals("Freelancer") 
                                && d.findElement(By.id("email")).getAttribute("value").equals(currentUsername + "@mail.com")
            );
    }

    /**
     * Profile -> 1
     */
    @Then("be able to update any information I desire.")
    public void clickUpdateInfo() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) ->   d.findElement(By.id("update_btn")).isDisplayed()
            );

    }

    /**
     * Profile -> 3
     */
    @When("I submit any change to the information")
    public void submitProfileUpdate() {
        WebElement name = driver.findElement(By.id("name"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement cur_pwd = driver.findElement(By.id("cur_pwd"));
        WebElement new_pwd = driver.findElement(By.id("new_pwd"));
        WebElement new_pwd_conf = driver.findElement(By.id("new_pwd_conf"));
        name.sendKeys("Teste 2");
        email.clear();
        email.sendKeys("teste2@mail.com");
        cur_pwd.clear();
        cur_pwd.sendKeys("test");
        new_pwd.sendKeys("teste2");
        new_pwd_conf.sendKeys("teste2");
        driver.findElement(By.id("update_btn")).click();
    }

    /**
     * Profile -> 3
     */
    @Then("I should see all changes instantly.")
    public void seeProfileInfoUpdated() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) ->   d.findElement(By.id("name")).getAttribute("value").equals("Teste 2")
                                && d.findElement(By.id("email")).getAttribute("value").equals("teste2@mail.com")
            );
    }

    /**
     * Profile -> 4
     */
    @When("I submit any change to the information without the correct format")
    public void submitProfileUpdateIncorrect() {
        WebElement name = driver.findElement(By.id("name"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement cur_pwd = driver.findElement(By.id("cur_pwd"));
        WebElement new_pwd = driver.findElement(By.id("new_pwd"));
        WebElement new_pwd_conf = driver.findElement(By.id("new_pwd_conf"));
        name.sendKeys("Teste 3");
        email.clear();
        email.sendKeys("teste3@mail.com");
        cur_pwd.clear();
        cur_pwd.sendKeys("pwd errada");
        new_pwd.sendKeys("teste3");
        new_pwd_conf.sendKeys("teste3");
        driver.findElement(By.id("update_btn")).click();
    }

    /**
     * Profile -> 4
     */
    @And("have the chance to correct my update submission.")
    public void canCorrectFormUpdate(){
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until(
                (WebDriver d) -> d.findElement(By.id("name")).isDisplayed()
            );
        assertTrue(driver.findElements(By.id("name")).size() > 0);
        assertTrue(driver.findElements(By.id("email")).size() > 0);
        assertTrue(driver.findElements(By.id("cur_pwd")).size() > 0);
    }

    /*
        The 3 steps below are not yet supported by the system
    */

    /**
     * Profile -> 2
     */
    @When("I'm on another user profile page")
    public void accessUserProfileInfo() {
        // TODO
    }

    /**
     * Profile -> 2
     */
    @Then("I should see the information he\\/she made available")
    public void seeUserProfileInfo() {
        // TODO
    }

    /**
     * Profile -> 2
     */
    @And("be able to start a conversation.")
    public void startConversation() {
        // TODO
    }

    /* ============================== SEARCHPROPOSAL TEST ============================== */

    /**
     * SearchProposal -> all
     * SearchUser -> all
     */
    @When("I access the search tab")
    public void accessTheSearchTab() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) -> d.findElement(By.id("search")).isDisplayed()
            );

        long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);

        driver.findElement(By.id("search")).click();

        jobService.registerOffer(currentUsername, new Job("title_test", "description_test", "java", 100, "2019-01-01", "Proposal"));
        jobService.registerOffer(currentUsername, new Job("title_test", "description_test", "java", 100, "2019-01-01", "Offer"));
        
    }

    /**
     * SearchProposal -> all
     */
    @And("choose the option of job proposals for freelancers")
    public void chooseJobProposals() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) -> d.findElement(By.id("searchByJobs")).isDisplayed()
            );

        driver.findElement(By.id("searchByJobs")).click();
    }

    /**
     * SearchProposal -> all
     */
    @And("I type in one or more keywords like the name of a programming language")
    public void searchKeywords() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until(
                (WebDriver d) -> {
                    WebElement query = driver.findElement(By.id("query"));
                    if (!query.isDisplayed()) {
                        return false;
                    }
                    query.clear();
                    query.sendKeys("java");
                    return true;
                }
            );
        driver.findElement(By.id("search_btn")).click();
    }

    /**
     * SearchProposal -> 1
     */
    @Then("I should see a list job proposals related to that keyword.")
    public void viewJobProposals() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .until(
                d -> {
                    List<WebElement> offers = d.findElements(By.className("blog-box"));
                    if (offers.size() == 0)
                        return false;
                    return offers.get(0).isDisplayed();
                }
            );
    }

    /**
     * SearchProposal -> 2
     */
    @And("I choose a filtering option")
    public void chooseFilteringOption() {
        driver.findElement(By.id("filters_btn")).click();
        driver.findElement(By.id("fromAmount")).clear();
        driver.findElement(By.id("fromAmount")).sendKeys("10");
        driver.findElement(By.id("toAmount")).clear();
        driver.findElement(By.id("toAmount")).sendKeys("1000");
    }

    /**
     * SearchProposal -> 2
     */
    @Then("I should see a list of job proposals filtered according to the chosen rule.")
    public void viewJobProposals2() {
        long previous = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-previous > 5000);
        new WebDriverWait(driver, MAX_WAIT_TIME).until(
            d -> {
                List<WebElement> offers = d.findElements(By.className("blog-box"));
                if (offers.isEmpty())
                    return false;
                return offers.get(0).isDisplayed();
            }
        );
    }

    /**
     * SearchProposal -> 3
     */
    @And("the results of the search are presented")
    public void viewJobProposals3() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .until(
                d -> {
                    List<WebElement> offers = d.findElements(By.className("blog-box"));
                    if (offers.size() == 0)
                        return false;
                    return offers.get(0).isDisplayed();
                }
            );
    }

    /**
     * SearchProposal -> 3
     */
    @And("I click on one job")
    public void clickJobOffer() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .until(
                d -> {
                    d.findElements(By.className("blog-box")).get(0).click();
                    return true;
                }
            );
    }

    /**
     * SearchProposal -> 3
     */
    @Then("I should see all information related to that job")
    public void seeInformationRelatedToJob() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until(
                (WebDriver d) -> driver.findElement(By.id("m1o")).isDisplayed()
            );
    }

    /**
     * SearchProposal -> 3
     */
    @And("I should be able to contact the proposal's author.")
    public void contactAuthorProposal() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until(
                (WebDriver d) -> driver.findElement(By.id("interest_btn")).isDisplayed()
            );
    }
    
    /* ============================== SEARCHOFFER TEST ============================== */
    
    /*
    @Given("that I am logged in,") -> EmployerPostJob Steps
    */

    /*
    @When("I access the search tab") -> SearchOffer Steps
    */

    /**
     * SearchOffer -> all
     */
    @And("choose the option of job offers for employers")
    public void chooseJobOffers() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) -> d.findElement(By.id("searchByJobs")).isDisplayed()
            );

        driver.findElement(By.id("searchByJobs")).click();
    }
    
    /*
     @And("I type in one or more keywords like the name of a programming language")
     */
    
    /**
     * SearchOffer -> 1
     */
    @Then("I should see a list job offers related to that keyword.")
    public void viewJobOffers() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .until(
                d -> {
                    List<WebElement> offers = d.findElements(By.className("blog-box"));
                    if (offers.isEmpty())
                        return false;
                    return offers.get(0).isDisplayed();
                }
            );
    }
    
    /*
     * SearchOffer -> 2
     */
    @Then("I should see a list of job offers filtered according to the chosen rule.")
    public void viewJobOffers2() {
        new WebDriverWait(driver, MAX_WAIT_TIME).until(
            d -> {
                List<WebElement> offers = d.findElements(By.className("blog-box"));
                if (offers.isEmpty())
                    return false;
                return offers.get(0).isDisplayed();
            }
        );
    }
    
    /*
     * SearchOffer -> 3
     */
    @And("the results of the offer search are presented")
    public void viewJobOffers3() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .until(
                d -> {
                    List<WebElement> offers = d.findElements(By.className("blog-box"));
                    if (offers.isEmpty())
                        return false;
                    return offers.get(0).isDisplayed();
                }
            );
    }
    
    /*
     * SearchOffer -> 3
     */
    @And("I should be able to contact the freelancer in order to hire him\\/her.")
    public void contactAuthorOffer() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until(
                (WebDriver d) -> driver.findElement(By.id("interest_btn")).isDisplayed()
            );
    }
            
    
    /* ============================== SEARCHUSER TEST ============================== */

    /*
    @Given("that I am logged in,") -> EmployerPostJob Steps
    */

    /*
    @When("I access the search tab") -> SearchOffer Steps
    */

    /**
     * SearchUser -> all
     */
    @And("choose the option of search for freelancers or employers")
    public void chooseUserSearch() {
        driver.findElement(By.id("searchByUsers")).click();

        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id("searchUserName")).isDisplayed()
            );
    }

    /**
     * SearchUser -> 2
     */
    @And("I type in one or more keywords like the name of a technology field")
    public void insertUserSearchFilters() {
        driver.findElement(By.id("filters_btn")).click();
        new Select(driver.findElement(By.id("searchUserType"))).selectByVisibleText("Freelancer");
    }

    /**
     * SearchUser -> all
     */
    @And("I click the search button")
    public void clickSearchButton() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .until(
                (WebDriver d) -> {
                    driver.findElement(By.id("search_btn")).click();
                    return true;
                }
        );
    }

    /**
     * SearchUser -> 1
     */
    @Then("I should see a list freelancers or employers")
    public void userPresentedAfterSearch() {
        // because this stories uses "that I am logged in," where a registration of a new user
        //  happens at least one user has exits
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .until(
                (WebDriver d) -> driver.findElements(By.className("userSearchResult")).size() >= 1
            );
    }

    /**
     * SearchUser -> 2
     */
    @Then("I should see a list freelancers or employers related to that\\/those keyword\\(s).")
    public void userPresentedAfterSearchFiltered() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .until(
                (WebDriver d) -> driver.findElements(By.className("userSearchResult")).size() >= 1
            );

        for (WebElement name : driver.findElements(By.className("userSearchType"))) {
            assertEquals("Freelancer", name.getText());
        }
    }

    /**
     * SearchUser -> 3
     */
    @And("the results are presented")
    public void resultsAppearAfterUserSearch() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .until(
                (WebDriver d) -> driver.findElements(By.className("userSearchResult")).size() >= 1
            );
    }

    /**
     * SearchUser -> 3
     */
    @And("I click on member")
    public void clickOnUserSearchResult() {
        driver.findElements(By.className("userSearchResult")).get(0).click();
    }

    /**
     * SearchUser -> 3
     */
    @Then("I should see all information related to him\\/her including possible job offers\\/proposals")
    public void userSearchInfoModal() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until(
                (WebDriver d) -> driver.findElement(By.id("userModal")).isDisplayed()
            );
    }

    /**
     * SearchUser ->
     */
    @And("I should be able to contact the member.")
    public void ableToContactTheMember() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .until(
                (WebDriver d) -> driver.findElement(By.id("contact_btn")).isDisplayed()
            );
    }

    /* ============================== EDIT POSTS TEST ============================== */
    
    /*
        @Given("that I am logged in,") -> EmployerPostJob Steps
    */
    
    /*
        @And("I have access to MyJobs page,") - EmployerPostJob Steps
    */

    /**
     * EditJob -> 1
     */
    @Given("I have one or more posts published,")
    public void havePostsPublished() {
        //new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
        //        (WebDriver d) -> d.findElement(By.id("createJobButton")).isDisplayed());
        assertTrue(driver.findElement(By.id("createJobButton")).isDisplayed());
        driver.findElement(By.id("createJobButton")).click();

        long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);

        WebElement title = driver.findElement(By.id("offerTitle"));
        WebElement description = driver.findElement(By.id("offerDescription"));
        WebElement area = driver.findElement(By.id("offerArea"));
        WebElement amount = driver.findElement(By.id("offerAmount"));
        WebElement date = driver.findElement(By.id("offerDate"));
        title.sendKeys("t1");
        description.sendKeys("t1");
        area.sendKeys("t1");
        amount.sendKeys("1");
        date.sendKeys("10-10-1111");

        driver.findElement(By.id("submitOfferButton")).click();

        //wait for modal animation
        long previous = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-previous > 1000);

        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.linkText("t1")).isDisplayed());
        //assertTrue(driver.findElement(By.linkText("t1")).isDisplayed());
    }

    /**
     * EditJob -> 1
     */
    @When("I choose the option to edit a job")
    public void chooseEditOption() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
                .ignoring(ElementClickInterceptedException.class)
                .until(d -> {
                    d.findElement(By.linkText("t1")).click();
                    return true;
                });
        
        new WebDriverWait(driver, MAX_WAIT_TIME)
                .ignoring(ElementNotVisibleException.class)
                .ignoring(ElementNotInteractableException.class)
                .until(d -> {
                    d.findElement(By.id("edit_save_btn")).click();
                    return true;
                });
    }

    /**
     * EditJob -> 1
     */
    @Then("I should see a form prefilled with the current data.")
    public void seePrefilledForm() {
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("modalTitle")).isDisplayed());
        assertTrue(driver.findElement(By.id("modalTitle")).getAttribute("value").equals("t1"));
        assertTrue(driver.findElement(By.id("modalDescription")).getAttribute("value").equals("t1"));
        assertTrue(driver.findElement(By.id("modalArea")).getAttribute("value").equals("t1"));
        assertTrue(driver.findElement(By.id("modalAmount")).getAttribute("value").equals("1"));
        assertTrue(driver.findElement(By.id("modalDate")).getAttribute("value").equals("1111-10-10"));
    }

    /**
     * EditJob -> 2
     */
    @When("I execute the previous edit steps")
    public void executePreviousEditSteps() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
                .ignoring(StaleElementReferenceException.class)
                .until((ExpectedCondition<Boolean>) 
                    (WebDriver d) -> d.findElement(By.id("jobs")).isDisplayed());

        long before = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-before > MAX_WAIT_TIME);

        driver.findElement(By.id("jobs")).click();
        
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("createJobButton")).isDisplayed());

        driver.findElement(By.id("createJobButton")).click();

        long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > MAX_WAIT_TIME);

        WebElement title = driver.findElement(By.id("offerTitle"));
        WebElement description = driver.findElement(By.id("offerDescription"));
        WebElement area = driver.findElement(By.id("offerArea"));
        WebElement amount = driver.findElement(By.id("offerAmount"));
        WebElement date = driver.findElement(By.id("offerDate"));
        title.sendKeys("t2");
        description.sendKeys("t2");
        area.sendKeys("t2");
        amount.sendKeys("2");
        date.sendKeys("1020-12-12");
        long previous = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
                (WebDriver d) -> System.currentTimeMillis()-previous > MAX_WAIT_TIME);
        driver.findElement(By.id("submitOfferButton")).click();
        
        long then4 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then4 > MAX_WAIT_TIME*4);
        
        new WebDriverWait(driver, MAX_WAIT_TIME*4)
                .ignoring(ElementClickInterceptedException.class)
                .until(d -> {
                    d.findElement(By.linkText("t2")).click();
                    return true;
                });
        
        new WebDriverWait(driver, MAX_WAIT_TIME)
                .ignoring(ElementNotVisibleException.class)
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(ElementNotInteractableException.class)
                .until(d -> {
                    d.findElement(By.id("edit_save_btn")).click();
                    return true;
                });
    }

    /**
     * EditJob -> 2
     */
    @And("I edit and submit the form,")
    public void editAndSubmitForm() {
        
        Long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 4000);
        
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("modalTitle")).isDisplayed());
        WebElement title = driver.findElement(By.id("modalTitle"));
        title.sendKeys("t1_edited");
        driver.findElement(By.id("edit_save_btn")).click();
    }

    /**
     * EditJob -> 2
     */
    @Then("I should see a message informing me about the success/failure of the operation")
    public void operationInfoMessage() {
        Long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 4000);
        
        assertTrue(driver.findElement(By.id("edit_save_btn")).getText().equals("Edit"));
        // to be continued...
    }

    /**
     * EditJob -> 2
     */
    @And("\\(if successful) I should see the updates on my posts.")
    public void seePostsUpdated() {
        driver.findElement(By.id("modalClose")).click();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> !d.findElement(By.id("modalClose")).isDisplayed());
    }
    
    /* ============================== MESSAGECENTER TEST ============================== */
    
    /*
        @Given("that I am logged in,") -> EmployerPostJob Steps
    */
    
    @When("I'm on the messaging center page")
    public void enterMessagingCenter() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("messages")).isDisplayed()
            );

        Long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);

        driver.findElement(By.id("messages")).click();

        String loggedInUsername = currentUsername;

        
        Message msg = new Message();
        msg.setDate(System.currentTimeMillis());
        msg.setDestination(currentUsername);
        msg.setMessage("test msg");
        msg.setOrigin(loggedInUsername);
        messageService.writeMessageByUsername(msg, loggedInUsername);
        
        driver.get("http://localhost:8080/messagesPage");

        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id(currentUsername)).isDisplayed()
            );
    }
    
    @Then("I should see the conversations I had with other members sorted by latest message")
    public void seeConversations() {
        assertTrue(driver.findElement(By.id(currentUsername)).isDisplayed());
    }
    
    @And("be able to click on one of the conversations.")
    public void canClickConversation() {
        assertTrue(driver.findElement(By.id(currentUsername)).isEnabled());
        
    }
    
    @When("I click on one of the conversations")
    public void clickConversation() {
        
        driver.findElement(By.id(currentUsername)).click();
        
        Long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);
    }
    
    @Then("I should see the messages I traded with the other member sorted by latest message")
    public void seeTradedMessages() {
        assertTrue(driver.getPageSource().contains("test msg"));
    }
    
    @And("be able to send him\\/her a new message.")
    public void canSendNewMessage() {
        assertTrue(driver.findElement(By.id("msgText")).isDisplayed());
        assertTrue(driver.findElement(By.id("msgText")).isEnabled());
        
    }
    
    /* ============================== USERSHOWSINTERESTPOST TEST ============================== */
    
    /*
        @Given("that I am logged in,") -> EmployerPostJob Steps
    */
    
    @And("I have accessed the intended Employer’s profile,")
    public void accessEmployerProfile() {
        
        // insert employer on database
        String newUsername = currentUsername + "2";
        User user2 = new User(
            newUsername,
            "test" + "2",
            newUsername,
            newUsername + "@mail.com",
            "Employer",
            new ArrayList<>()
        );
        authenticationService.register(user2);
        
        // add job offer to employer
        Job jo = new Job("title_test_2", "description_test_2", "java", 100, "2019-01-01", "Offer");
        jobService.registerOffer(newUsername, jo);
        // add job offer to freelancer
        Job jo2 = new Job("title_test_3", "description_test_3", "java", 100, "2019-01-01", "Proposal");
        jobService.registerOffer(currentUsername, jo2);
        
        // go to search tab
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) -> d.findElement(By.id("search")).isDisplayed()
            );
        Long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);
        driver.findElement(By.id("search")).click();
        
        // filter users only
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id("searchByUsers")).isDisplayed()
            );
        driver.findElement(By.id("searchByUsers")).click();
        
        // search for employer
        driver.findElement(By.id("searchUserName")).sendKeys(newUsername);
        driver.findElement(By.id("search_btn")).click();
        
        // click employer
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id(newUsername)).isDisplayed()
            );
        driver.findElement(By.id(newUsername)).click();
        Long then2 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then2 > 1000);
    }
    
    @And("I click on his\\/her job offer,")
    public void clickEmployerJobOffer() {
        driver.findElement(By.id("title_test_2")).click();
        Long then3 = System.currentTimeMillis();
        new WebDriverWait(driver,MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then3 > 1000);
        
    }
    
    @When("I click on the contact button")
    public void clickShowInterest() {
        driver.findElement(By.id("interest_btn")).click();
        Long then4 = System.currentTimeMillis();
        new WebDriverWait(driver,MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then4 > 1000);
        
    }
    
    @Then("I should be redirected to the message center’s conversation with the Employer")
    public void redirectToEmployerConversation() {
        assertTrue(driver.findElement(By.id("messageCenterLobby")).isDisplayed());
        assertTrue(driver.findElement(By.id(currentUsername + "2")).isDisplayed());
        driver.findElement(By.id(currentUsername + "2")).click();
    }
    
    @And("I should see an automatic private message mentioning the interest.")
    public void seeAutomaticInterestedMessage() {
        Long then4 = System.currentTimeMillis();
        new WebDriverWait(driver,MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then4 > 1000);
        assertTrue(driver.findElement(By.id("title_test_2")).isDisplayed());
        
    }
    
    @And("a Freelancer has shown interest in one of my posts,")
    public void freelancerShowedInterest() {
        // freelancer shows interest and logs out
        
        // insert employer on database
        String newUsername = currentUsername + "2";
        User user2 = new User(
            newUsername,
            "test" + "2",
            newUsername,
            newUsername + "@mail.com",
            "Employer",
            new ArrayList<>()
        );
        authenticationService.register(user2);
        
        // add job offer to employer
        Job jo = new Job("title_test_2", "description_test_2", "java", 100, "2019-01-01", "Offer");
        jobService.registerOffer(newUsername, jo);
        // add job offer to freelancer
        Job jo2 = new Job("title_test_3", "description_test_3", "java", 100, "2019-01-01", "Proposal");
        jobService.registerOffer(currentUsername, jo2);
        
        // go to search tab
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) -> d.findElement(By.id("search")).isDisplayed()
            );
        Long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);
        driver.findElement(By.id("search")).click();
        
        // filter users only
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id("searchByUsers")).isDisplayed()
            );
        driver.findElement(By.id("searchByUsers")).click();
        
        // search for employer
        driver.findElement(By.id("searchUserName")).sendKeys(newUsername);
        driver.findElement(By.id("search_btn")).click();
        
        // click employer
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id(newUsername)).isDisplayed()
            );
        driver.findElement(By.id(newUsername)).click();
        Long then2 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then2 > 1000);
        
        
        
        
        
        driver.findElement(By.id("title_test_2")).click();
        Long then3 = System.currentTimeMillis();
        new WebDriverWait(driver,MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then3 > 1000);
        
        
        
        
        driver.findElement(By.id("interest_btn")).click();
        Long then4 = System.currentTimeMillis();
        new WebDriverWait(driver,MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then4 > 1000);
        
        
        
        
        driver.findElement(By.id("signout")).click();
        
        // employer logs in and goes to message center
        String newUsername1 = currentUsername + "2";
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id("username")).isDisplayed()
            );
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys(newUsername1);
        pwd.clear();
        pwd.sendKeys("test2");
        driver.findElement(By.id("submit_button")).click();
        
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("messages")).isDisplayed()
            );
        Long then5 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then5 > 5000);
        driver.findElement(By.id("messages")).click();
        
        // checks freelancer's interest
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("messageCenterLobby")).isDisplayed()
            );
        
        Long then6 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then6 > 6000);
        
        assertTrue(driver.findElement(By.id("cucumber_tests"+(usernameCount))).isDisplayed());
    }
    
    @And("I click on the conversation with the Freelancer,")
    public void clickConversationWithFreelancer() {
        driver.findElement(By.id("cucumber_tests"+(usernameCount))).click();
    }

    /* ============================== USERPROPOSESMESSAGECENTER TEST ============================== */

    /*
        @Given("that I am logged in,") -> EmployerPostJob Steps
    */
    
    @And("I have accessed the message center’s conversation with the employer")
    public void accessConversationWithEmployer() {
        // show interest in job
        // insert employer on database
        String newUsername = currentUsername + "2";
        User user2 = new User(
            newUsername,
            "test" + "2",
            newUsername,
            newUsername + "@mail.com",
            "Employer",
            new ArrayList<>()
        );
        authenticationService.register(user2);
        
        // add job offer to employer
        Job jo = new Job("title_test_2", "description_test_2", "java", 100, "2019-01-01", "Offer");
        jobService.registerOffer(newUsername, jo);
        // add job offer to freelancer
        Job jo2 = new Job("title_test_3", "description_test_3", "java", 100, "2019-01-01", "Proposal");
        jobService.registerOffer(currentUsername, jo2);
        
        // go to search tab
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) -> d.findElement(By.id("search")).isDisplayed()
            );
        Long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);
        driver.findElement(By.id("search")).click();
        
        // filter users only
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id("searchByUsers")).isDisplayed()
            );
        driver.findElement(By.id("searchByUsers")).click();
        
        // search for employer
        driver.findElement(By.id("searchUserName")).sendKeys(newUsername);
        driver.findElement(By.id("search_btn")).click();
        
        // click employer
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id(newUsername)).isDisplayed()
            );
        driver.findElement(By.id(newUsername)).click();
        Long then2 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then2 > 1000);
        
        
        
        driver.findElement(By.id("title_test_2")).click();
        Long then3 = System.currentTimeMillis();
        new WebDriverWait(driver,MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then3 > 1000);
        
        
        
        
        driver.findElement(By.id("interest_btn")).click();
        Long then4 = System.currentTimeMillis();
        new WebDriverWait(driver,MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then4 > 1000);
    }
    
    @When("I click on the plus button")
    public void clickPlusButton() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("plus_btn")).isDisplayed()
            );
        driver.findElement(By.id("plus_btn")).click();
    }
    
    @And("I select one of our job proposals")
    public void selectJob() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("title_test_3")).isDisplayed()
            );
        driver.findElement(By.id("title_test_3")).click();
    }
    
    @Then("I should see an automatic private message mentioning the pending hiring proposal.")
    public void seeAutomaticMessagePendingHiring() {
        Long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);
        assertTrue(driver.findElement(By.id("msg_title_test_3")).isDisplayed());
    }
    
    /*
        @When("I'm on the messaging center page") -> MessageCenter Steps
    */
    
    @And("a Freelancer has sent a hiring proposal to me")
    public void freelancerSentProposal() {
        // show interest and send hiring proposal
        String newUsername = currentUsername + "2";
        User user2 = new User(
            newUsername,
            "test" + "2",
            newUsername,
            newUsername + "@mail.com",
            "Employer",
            new ArrayList<>()
        );
        authenticationService.register(user2);
        
        // add job offer to employer
        Job jo = new Job("title_test_2", "description_test_2", "java", 100, "2019-01-01", "Offer");
        jobService.registerOffer(newUsername, jo);
        // add job offer to freelancer
        Job jo2 = new Job("title_test_3", "description_test_3", "java", 100, "2019-01-01", "Proposal");
        jobService.registerOffer(currentUsername, jo2);
        
        // go to search tab
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) -> d.findElement(By.id("search")).isDisplayed()
            );
        Long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);
        driver.findElement(By.id("search")).click();
        
        // filter users only
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id("searchByUsers")).isDisplayed()
            );
        driver.findElement(By.id("searchByUsers")).click();
        
        // search for employer
        driver.findElement(By.id("searchUserName")).sendKeys(newUsername);
        driver.findElement(By.id("search_btn")).click();
        
        // click employer
        String newUsername1 = currentUsername + "2";
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id(newUsername1)).isDisplayed()
            );
        driver.findElement(By.id(newUsername)).click();
        Long then2 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then2 > 1000);
        
        
        Long then4 = System.currentTimeMillis();
        driver.findElement(By.id("title_test_2")).click();
        new WebDriverWait(driver,MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then4 > 1000);
        
        
        
        
        driver.findElement(By.id("interest_btn")).click();
        Long then5 = System.currentTimeMillis();
        new WebDriverWait(driver,MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then5 > 1000);
        
        
        
        
        
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("plus_btn")).isDisplayed()
            );
        driver.findElement(By.id("plus_btn")).click();
        
        
        
        
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("title_test_3")).isDisplayed()
            );
        driver.findElement(By.id("title_test_3")).click();
        
        
        
        
        driver.findElement(By.id("signout")).click();
        
        // employer logs in and goes to message center
        newUsername = currentUsername + "2";
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id("username")).isDisplayed()
            );
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys(newUsername);
        pwd.clear();
        pwd.sendKeys("test2");
        driver.findElement(By.id("submit_button")).click();
        
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("messages")).isDisplayed()
            );
        
        
        Long then6 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then6 > 1000);
        driver.findElement(By.id("messages")).click();
        
        // checks freelancer's interest
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("messageCenterLobby")).isDisplayed()
            );
        
        Long then7 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then7 > 4000);
        assertTrue(driver.findElement(By.id("cucumber_tests"+(usernameCount))).isDisplayed());
    }
    
    @Then("I should see his\\/her hiring proposal")
    public void seeHiringPorposal() {
        driver.findElement(By.id("cucumber_tests"+(usernameCount))).click();
        // ...
    }
    
    @And("be able to accept or deny it.")
    public void answerHiringProposal() {
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("accept_title_test_3")).isDisplayed()
            );
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("deny_title_test_3")).isDisplayed()
            );
    }
    
    @And("he\\/she has responded to my request")
    public void employerRespondedToProposal() {
        // show interest and send hiring proposal
        String newUsername = currentUsername + "2";
        User user2 = new User(
            newUsername,
            "test" + "2",
            newUsername,
            newUsername + "@mail.com",
            "Employer",
            new ArrayList<>()
        );
        authenticationService.register(user2);
        
        // add job offer to employer
        Job jo = new Job("title_test_2", "description_test_2", "java", 100, "2019-01-01", "Offer");
        jobService.registerOffer(newUsername, jo);
        // add job offer to freelancer
        Job jo2 = new Job("title_test_3", "description_test_3", "java", 100, "2019-01-01", "Proposal");
        jobService.registerOffer(currentUsername, jo2);
        
        // go to search tab
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) -> d.findElement(By.id("search")).isDisplayed()
            );
        Long then = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then > 1000);
        driver.findElement(By.id("search")).click();
        
        // filter users only
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id("searchByUsers")).isDisplayed()
            );
        driver.findElement(By.id("searchByUsers")).click();
        
        // search for employer
        driver.findElement(By.id("searchUserName")).sendKeys(newUsername);
        driver.findElement(By.id("search_btn")).click();
        
        // click employer
        String newUsername1 = currentUsername + "2";
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id(newUsername1)).isDisplayed()
            );
        driver.findElement(By.id(newUsername)).click();
        Long then2 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then2 > 1000);
        
        
        Long then4 = System.currentTimeMillis();
        driver.findElement(By.id("title_test_2")).click();
        new WebDriverWait(driver,MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then4 > 1000);
        
        
        
        
        driver.findElement(By.id("interest_btn")).click();
        Long then5 = System.currentTimeMillis();
        new WebDriverWait(driver,MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then5 > 1000);
        
        
        
        
        
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("plus_btn")).isDisplayed()
            );
        driver.findElement(By.id("plus_btn")).click();
        
        
        
        
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("title_test_3")).isDisplayed()
            );
        driver.findElement(By.id("title_test_3")).click();
        
        
        
        
        driver.findElement(By.id("signout")).click();
        
        // employer logs in and goes to message center
        newUsername = currentUsername + "2";
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id("username")).isDisplayed()
            );
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys(newUsername);
        pwd.clear();
        pwd.sendKeys("test2");
        driver.findElement(By.id("submit_button")).click();
        
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("messages")).isDisplayed()
            );
        
        
        Long then6 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then6 > 1000);
        driver.findElement(By.id("messages")).click();
        
        // checks freelancer's interest
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("messageCenterLobby")).isDisplayed()
            );
        
        Long then7 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then7 > 6000);
        
        
        assertTrue(driver.findElement(By.id("cucumber_tests"+(usernameCount))).isDisplayed());
        
        driver.findElement(By.id("cucumber_tests"+(usernameCount))).click();
        
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("accept_title_test_3")).isDisplayed()
            );
        
        
        driver.findElement(By.id("accept_title_test_3")).click();
        
    }
    
    @Then("I should see the response.")
    public void seeResponse() {
        
        driver.findElement(By.id("signout")).click();
        
        // employer logs in and goes to message center
        String newUsername = currentUsername + "2";
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .until(
                (WebDriver d) ->  d.findElement(By.id("username")).isDisplayed()
            );
        WebElement username = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("pwd"));
        username.sendKeys("cucumber_tests"+(usernameCount));
        pwd.clear();
        pwd.sendKeys("test");
        driver.findElement(By.id("submit_button")).click();
        
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("messages")).isDisplayed()
            );
        
        
        Long then6 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then6 > 1000);
        driver.findElement(By.id("messages")).click();
        
        
        // checks freelancer's interest
        new WebDriverWait(driver, MAX_WAIT_TIME)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("messageCenterLobby")).isDisplayed()
            );
        
        Long then7 = System.currentTimeMillis();
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> System.currentTimeMillis()-then7 > 1000);
        
        assertTrue(driver.findElement(By.id("cucumber_tests"+(usernameCount)+"2")).isDisplayed());
        
        driver.findElement(By.id("cucumber_tests"+(usernameCount)+"2")).click();
        
        
        new WebDriverWait(driver, MAX_WAIT_TIME).until((ExpectedCondition<Boolean>)
            (WebDriver d) -> d.findElement(By.id("title_test_3_accepted")).isDisplayed());

    }
}
