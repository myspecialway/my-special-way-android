package org.myspecialway.ui.login;


import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myspecialway.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Before
    public void before() {
        Utils.clearPreferences(activityTestRule.getActivity());
    }


    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void passwordIsEmpty() {
        onView(withId(R.id.usernameTextFiled)).perform(typeText("stu"), closeSoftKeyboard());
        onView(withId(R.id.passwordTextFiled)).perform(clearText());
        onView(withId(R.id.loginButton)).check(matches(not(isEnabled())));
    }

    @Test
    public void userIsEmpty() {
        onView(withId(R.id.passwordTextFiled)).perform(typeText("Aa12"), closeSoftKeyboard());
        onView(withId(R.id.usernameTextFiled)).perform(clearText());
        onView(withId(R.id.loginButton)).check(matches(not(isEnabled())));
    }

    /**
     * Logic Changed, the progress view is not visible anymore, it was replaced with different loader
     * @throws InterruptedException
     */
//    @Test
//    public void progressBarVisibleAfterLoginClick() throws InterruptedException {
//        onView(withId(R.id.usernameTextFiled)).perform(typeText("1"), closeSoftKeyboard());
//        onView(withId(R.id.passwordTextFiled)).perform(typeText("1"), closeSoftKeyboard());
//
//        Thread.sleep(500);
//        onView(withId(R.id.loginButton)).perform(click());
//
//        onView(withId(R.id.progress)).check(matches(isDisplayed()));
//    }

    @Test
    public void checkErrorDialogDisplayedOnBadCredentials() throws InterruptedException {
        onView(withId(R.id.usernameTextFiled)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.passwordTextFiled)).perform(typeText("1"), closeSoftKeyboard());

        Thread.sleep(500);
        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(4000);

        onView(withId(R.id.closeButton)).check(matches(isDisplayed()));
    }

    @Test
    public void checkUserNameFocus() {
        onView(withId(R.id.usernameTextFiled)).check(matches(ViewMatchers.isFocusable()));
    }

    /**
     * cant login with these credentials
     */
//    @Test
//    public void loginPassed() throws InterruptedException {
//        onView(withId(R.id.usernameTextFiled)).perform(typeText("student"), closeSoftKeyboard());
//        onView(withId(R.id.passwordTextFiled)).perform(typeText("Aa123456"), closeSoftKeyboard());
//
//        Thread.sleep(500);
//        onView(withId(R.id.loginButton)).perform(click());
//
//        //Wait for a constant time of 2 seconds to get the response from server for login
//        Thread.sleep(4000);
//
//        assertTrue(activityTestRule.getActivity().isFinishing());
//
//    }


}
