package tqs.cloudit.mobile_app;

import org.junit.Rule;
import org.junit.Test;

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

public class JobsActivityTest {

    @Rule
    public ActivityTestRule<JobsActivity> mActivityTestRule = new ActivityTestRule<JobsActivity>(JobsActivity.class);

    @Test
    public void testButtonsAndInputs() {
        onView(withId(R.id.searchText)).check(matches(isDisplayed()));
        onView(withId(R.id.searchButton)).check(matches(isDisplayed()));
        onView(withId(R.id.alarmButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearch() {
        onView(withId(R.id.searchText)).perform(typeText("AI"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, matches(withText(containsString("AI")))));
    }

    @Test
    public void testNonExistingItemSearch() {
        onView(withId(R.id.searchText)).perform(typeText("QWERTYUIOP"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(R.id.recyclerView)).check(matches(not(hasItem(hasDescendant(withText(containsString("QWERTYUIOP")))))));
    }

    @Test
    public void testJobsInfo() {
        onView(withId(R.id.searchText)).perform(typeText("AI"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.descriptionLabel)).perform(matches(isDisplayed()));
        onView(withId(R.id.descriptionValue)).perform(matches(isDisplayed()));
        onView(withId(R.id.areaLabel)).perform(matches(isDisplayed()));
        onView(withId(R.id.areaValue)).perform(matches(isDisplayed()));
    }
}
