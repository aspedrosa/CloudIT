package tqs.cloudit.services;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import tqs.cloudit.domain.rest.JobOffer;
import tqs.cloudit.domain.rest.User;

/**
 * Where all the steps of all features are defined here
 * Each step has referenced on javadoc on what Scenario(s) of which Feature(s) it is used
 */
@TestPropertySource (value={"classpath:application.properties"})
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StepsDefs {

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

    @Autowired
    private JobService jobService;

    /* ============================== SIGNIN TEST ============================== */

    /**
     * SignUp.feature -> all
     *
     * SignIn.feature -> all
     *
     * AboutPlatform.feature -> all
     */
    @Given("that I have access to the platform's website,")
    public void openWebsite() {
        driver.get("http://localhost:8080/loginPage");
    }

    /**
     * SignIn.feature -> 1
     *
     * AboutPlatform.feature -> 2
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
     * SignUp.feature -> 1
     *
     * SignIn.feature -> 1
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
     * SignIn.feature -> 2
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
     * SignUp.feature -> 2
     *
     * SignIn.feature -> 2
     */
    @Then("I should be notified about the invalid fields.")
    public void checkErrorMessage() {
        new WebDriverWait(driver,10L).until(
                (WebDriver d) -> d.findElements(By.id("invalid_credentials_message")).size() > 0);
        assertTrue(driver.findElements( By.id("invalid_credentials_message") ).size() > 0);
        assertEquals(driver.findElement( By.id("invalid_credentials_message") ).getText(), "Error! Invalid Credentials.");
        driver.quit();
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
     * SignUp.feature -> 2
     *
     * SignIn.feature -> 2
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
     * SignUp.feature -> 2
     */
    @And("have the chance to correct my submission.")
    public void canCorrectForm(){
        new WebDriverWait(driver,10L).until(
                (WebDriver d) -> d.findElement(By.id("name")).isDisplayed());
        assertTrue(driver.findElements(By.id("name")).size() > 0);
        assertTrue(driver.findElements(By.id("email")).size() > 0);
        assertTrue(driver.findElements(By.id("pwd")).size() > 0);
        driver.quit();
    }
    
    /* ============================== ABOUT TEST ============================== */
    
    /*
    @Given("that I have access to the platform's website,") -> Sign In Steps
    */
    
    /*
    @When("I login") -> Sign In Steps
    */

    /**
     *  AboutPlatform.feature -> all
     */
    @When("I click on the about tab")
    public void clickAbout() {
        new WebDriverWait(driver, 10L)
            .ignoring(StaleElementReferenceException.class)
            .until(driver -> {
                driver.findElement(By.id("about")).click();
                return true;
            });
    }

    /**
     *  AboutPlatform.feature -> all
     */
    @Then("I should see the about page.")
    public void checkAboutPage() {
        new WebDriverWait(driver, 10L)
            .ignoring(NoSuchElementException.class)
            .until(driver ->
                driver.findElement(By.id("about_page_content")).isDisplayed()
            );
        driver.quit();
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
     */
    @Given("I have accessed to MyJobs page,")
    public void accessedMyJobs() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.id("jobs")).isDisplayed());
        driver.findElement(By.id("jobs")).click();
    }

    /**
     * EmployerPostJob -> 1
     */
    @When("I choose the option to post a job offer")
    public void chooseOptionPostOffer() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.id("createJobButton")).isDisplayed());
        driver.findElement(By.id("createJobButton")).click();
    }

    /**
     * EmployerPostJob -> 1
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
     * EmployerPostJob -> 2
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
     * EmployerPostJob -> 2
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
     * EmployerPostJob -> 2
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
     * EmployerPostJob -> 2
     */
    @Then("\\(if successful) I should see a new post added to my profile.")
    public void ifSuccessfulShouldSeeNewPostAddedToProfile() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) -> d.findElement(By.linkText("t1")).isDisplayed());
        driver.quit();
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
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>)
                (WebDriver d) -> d.findElement(By.id("profile")).isDisplayed());
        driver.findElement(By.id("profile")).click();
        assertTrue(driver.findElements(By.id("profile_form")).size() > 0);
        assertEquals(driver.findElement(By.id("profile_form_title")).getText(), "Profile");
    }

    /**
     * Profile -> 1
     */
    @Then("I should see the information that is visible to all members.")
    public void seeProfileInfo() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) ->   d.findElement(By.id("username")).getText().equals(currentUsername)
                                && d.findElement(By.id("type")).getText().equals("Freelancer") 
                                && d.findElement(By.id("email")).getAttribute("value").equals(currentUsername + "@mail.com"));
    }

    /**
     * Profile -> 1
     */
    @Then("be able to update any information I desire.")
    public void clickUpdateInfo() {
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) ->   d.findElement(By.id("update_btn")).isDisplayed());
        assertTrue(driver.findElements(By.id("update_btn")).size() > 0);
        driver.quit();
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
        new WebDriverWait(driver,10L).until((ExpectedCondition<Boolean>) 
                (WebDriver d) ->   d.findElement(By.id("name")).getAttribute("value").equals("Teste 2")
                                && d.findElement(By.id("email")).getAttribute("value").equals("teste2@mail.com"));
        driver.quit();
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
        name.sendKeys("Teste 2");
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
        new WebDriverWait(driver,10L).until(
                (WebDriver d) -> d.findElement(By.id("name")).isDisplayed());
        assertTrue(driver.findElements(By.id("name")).size() > 0);
        assertTrue(driver.findElements(By.id("email")).size() > 0);
        assertTrue(driver.findElements(By.id("cur_pwd")).size() > 0);
        driver.quit();
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

    /* ============================== SEARCHOFFER TEST ============================== */

    /**
     * SearchOffer -> all
     * SearchUser -> all
     */
    @When("I access the search tab")
    public void accessTheSearchTab() {
        new WebDriverWait(driver,10L).until(
                (WebDriver d) -> d.findElement(By.id("search")).isDisplayed());
        WebElement search_tab = driver.findElement(By.id("search"));
        search_tab.click();
        jobService.registerOffer(currentUsername, new JobOffer("title_test", "description_test", "java", 100, "2019-01-01"));
    }

    /**
     * SearchOffer -> all
     */
    @And("choose the option of job proposals for freelancers")
    public void chooseJobProposals() {
        driver.findElement(By.id("searchByJobs")).click();
    }

    /**
     * SearchOffer -> all
     */
    @And("I type in one or more keywords like the name of a programming language")
    public void searchKeywords() {
        WebElement query = driver.findElement(By.id("query"));
        new WebDriverWait(driver,10L).until(
                (WebDriver d) -> query.isDisplayed());
        query.clear();
        query.sendKeys("java");
        driver.findElement(By.id("search_btn")).click();
    }

    /**
     * SearchOffer -> 1
     */
    @Then("I should see a list job proposals related to that keyword.")
    public void viewJobProposals() {
        WebElement first_offer = driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='To:'])[2]/following::div[6]"));
        new WebDriverWait(driver,10L).until(
            (WebDriver d) -> first_offer.isDisplayed());
    }

    /**
     * SearchOffer -> 2
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
     * SearchOffer -> 2
     */
    @Then("I should see a list of job proposals filtered according to the chosen rule.")
    public void viewJobProposals2() {
        WebElement first_offer = driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='To:'])[2]/following::div[6]"));
        new WebDriverWait(driver,10L).until(
            (WebDriver d) -> first_offer.isDisplayed());
    }

    /**
     * SearchOffer -> 3
     */
    @And("the results of the search are presented")
    public void viewJobProposals3() {
        WebElement first_offer = driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='To:'])[2]/following::div[6]"));
        new WebDriverWait(driver,10L).until(
            (WebDriver d) -> first_offer.isDisplayed());
    }

    /**
     * SearchOffer -> 3
     */
    @And("I click on one job")
    public void clickJobOffer() {
        driver.findElements(By.className("blog-box")).get(0).click();
    }

    /**
     * SearchOffer -> 3
     */
    @Then("I should see all information related to that job")
    public void seeInformationRelatedToJob() {
        WebElement offer_modal = driver.findElement(By.id("m1"));
        new WebDriverWait(driver,10L).until(
            (WebDriver d) -> offer_modal.isDisplayed());
    }

    /**
     * SearchOffer -> 3
     */
    @And("I should be able to contact the proposal's author.")
    public void contactAuthorProposal() {
        WebElement contact_btn = driver.findElement(By.id("contact_btn"));
        new WebDriverWait(driver,10L).until(
            (WebDriver d) -> contact_btn.isDisplayed());
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

        new WebDriverWait(driver,10L).until(
            (WebDriver d) ->  d.findElement(By.id("searchUserName")).isDisplayed() &&
                              d.findElement(By.id("searchUserInterestedAreas")).isEnabled() &&
                              d.findElement(By.id("searchUserType")).isDisplayed() &&
                              d.findElement(By.id("allUsers")).isEnabled());
    }

    /**
     * SearchUser -> 2
     */
    @And("I type in one or more keywords like the name of a technology field")
    public void insertUserSearchFilters() {
        new Select(driver.findElement(By.id("searchUserType"))).selectByVisibleText("Freelancer");
    }

    /**
     * SearchUser -> all
     */
    @And("I click the search button")
    public void clickSearchButton() {
        driver.findElement(By.id("userSearchBtn")).click();
    }

    /**
     * SearchUser -> 1
     */
    @Then("I should see a list freelancers or employers")
    public void userPresentedAfterSearch() {
        // because this stories uses "that I am logged in," where a registration of a new user
        //  happens at least one user has exits
        new WebDriverWait(driver,10L).until(
            (WebDriver d) -> driver.findElements(By.className("userSearchResult")).size() >= 1);
        driver.quit();
    }

    /**
     * SearchUser -> 2
     */
    @Then("I should see a list freelancers or employers related to that\\/those keyword\\(s).")
    public void userPresentedAfterSearchFiltered() {
        new WebDriverWait(driver,10L).until(
            (WebDriver d) -> driver.findElements(By.className("userSearchResult")).size() >= 1);

        for (WebElement name : driver.findElements(By.className("userSearchType"))) {
            assertEquals("Freelancer", name.getText());
        }
        driver.quit();
    }

    /**
     * SearchUser -> 3
     */
    @And("the results are presented")
    public void resultsAppearAfterUserSearch() {
        new WebDriverWait(driver,10L).until(
            (WebDriver d) -> driver.findElements(By.className("userSearchResult")).size() >= 1);
    }

    /**
     * SearchUser -> 3
     */
    @And("I click on member")
    public void clickOnUserSearchResult() {
        assertFalse(driver.findElement(By.id("userModal")).isDisplayed());
        driver.findElements(By.className("userSearchResult")).get(0).click();
    }

    /**
     * SearchUser -> 3
     */
    @Then("I should see all information related to him\\/her including possible job offers\\/proposals")
    public void userSearchInfoModal() {
        new WebDriverWait(driver,10L).until(
            (WebDriver d) -> driver.findElement(By.id("userModal")).isDisplayed());
    }

    /**
     * SearchUser ->
     */
    @And("I should be able to contact the member.")
    public void ableToContactTheMember() {
        WebElement contact_btn = driver.findElement(By.id("contact_btn"));
        new WebDriverWait(driver,10L).until(
            (WebDriver d) -> contact_btn.isDisplayed());
        driver.quit();
    }

    /* ============================== TEST ============================== */

    // add new steps here...

}
