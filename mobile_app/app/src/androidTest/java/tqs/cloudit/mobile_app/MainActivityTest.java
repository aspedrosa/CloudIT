package tqs.cloudit.mobile_app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testButtonsAndInputs() {
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.submit)).check(matches(isDisplayed()));
    }

    @Test
    public void testLogin() {
        onView(withId(R.id.username)).perform(typeText("username"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());

        onView(withId(R.id.searchButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testFailedLogin() {
        onView(withId(R.id.username)).perform(typeText("nonexistingusername"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("nonexistingpassword"), closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());

        onView(withId(R.id.submit)).check(matches(isDisplayed()));
    }
}
