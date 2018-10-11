package org.myspecialway.ui.utils;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.myspecialway.R;
import org.myspecialway.ui.login.LoginActivity;
import org.myspecialway.ui.mainScreen.MainScreenActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class Utils {

    /**
     * @drieur
     * logout from application using settings logout.
     */


    public static void logOut(){

        new ActivityTestRule<>(MainScreenActivity.class).launchActivity(null);
onView(withId(R.id.settings)).perform(click());

        onView(nthChildOf(withId(android.R.id.list), 2)).perform(click());

        onView(withId(R.id.approveButton)).perform(click()); //todo logout fails on click approve button since ISHUR button does not completely displayed on the screen


    }


    /**
     * @drieur
     * have access to any child of a list.
     * @param parentMatcher
     * @param childPosition
     * @return
     */

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("position " + childPosition + " of parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) return false;
                ViewGroup parent = (ViewGroup) view.getParent();

                return parentMatcher.matches(parent)
                        && parent.getChildCount() > childPosition
                        && parent.getChildAt(childPosition).equals(view);
            }
        };
    }
//
//    public static Matcher<Object> withItemContent(String expectedText) {
//        checkNotNull(expectedText);
//        return withItemContent(equalTo(expectedText));
//    }


    /**
     * @drieur
     * login to the application.
     */
    public static void doLogin() {

        new ActivityTestRule<>(LoginActivity.class).launchActivity(null);

            onView(withId(R.id.usernameTextFiled)).perform(typeText("student"), closeSoftKeyboard());
            onView(withId(R.id.passwordTextFiled)).perform(typeText("Aa123456"), closeSoftKeyboard());

            onView(withId(R.id.loginButton)).perform(click());


// todo find a way for dynamic wait on espresso
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    /**
     * @drieur
     * open student's agenta
     */
    public static void openAgenda() {
        new ActivityTestRule<>(MainScreenActivity.class).launchActivity(null);
        boolean tempBool = true;

        try {
            onView(withId(R.id.scheduleButton)).check(matches(isDisplayed()));


        } catch (NoMatchingViewException e) {
            tempBool = false;
        }

        if (tempBool) {
            onView(withId(R.id.scheduleButton)).perform(click());
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }

            System.out.println(" opened schedule");
        }

    }




    /** todo take current hour from android device
     *
     */


}
