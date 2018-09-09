package org.myspecialway.ui.login;


import android.os.Handler;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myspecialway.R;
import org.myspecialway.common.Run;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void passwordIsEmpty() {
        onView(withId(R.id.usernameTextFiled)).perform(typeText("student"), closeSoftKeyboard());
        onView(withId(R.id.passwordTextFiled)).perform(clearText());
        onView(withId(R.id.loginButton)).check(matches(not(isEnabled())));
    }

    @Test
    public void userIsEmpty() {
        onView(withId(R.id.passwordTextFiled)).perform(typeText("student"), closeSoftKeyboard());
        onView(withId(R.id.usernameTextFiled)).perform(clearText());
        onView(withId(R.id.loginButton)).check(matches(not(isEnabled())));
    }


    @Test
    public void loginFailed() {
        onView(withId(R.id.usernameTextFiled)).perform(typeText("bad user"), closeSoftKeyboard());
        onView(withId(R.id.passwordTextFiled)).perform(typeText("11111"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }

}
