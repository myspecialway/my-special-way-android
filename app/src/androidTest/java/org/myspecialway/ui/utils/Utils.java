package org.myspecialway.ui.utils;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;

import org.myspecialway.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class Utils {

    public static void clearPreferences(Activity activity) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().apply();
    }





    public static void doLogin(ActivityTestRule activityTestRule) throws InterruptedException {
        activityTestRule.launchActivity(null);
        onView(withId(R.id.usernameTextFiled)).perform(typeText("student"), closeSoftKeyboard());
        onView(withId(R.id.passwordTextFiled)).perform(typeText("Aa123456"), closeSoftKeyboard());

        Thread.sleep(500);
        onView(withId(R.id.loginButton)).perform(click());

        //Wait for a constant time of 2 seconds to get the response from server for login
        Thread.sleep(4000);
    }


    public static void openSchedule() {

        onView(withId(R.id.scheduleButton)).perform(click());
        try {
            onView(withId(R.id.dayScheduleTitle)).wait(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(" opened schedule");
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

}
