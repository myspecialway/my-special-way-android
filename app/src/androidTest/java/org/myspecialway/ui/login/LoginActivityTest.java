package org.myspecialway.ui.login;


import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

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

    @Test
    public void loginFailed() {
        onView(withId(R.id.usernameTextFiled)).perform(typeText("bad user"), closeSoftKeyboard());
        onView(withId(R.id.passwordTextFiled)).perform(typeText("11111"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }

    @Test
    public void checkUserNameFocus() {
        onView(withId(R.id.usernameTextFiled)).check(matches(ViewMatchers.isFocusable()));
    }
}
