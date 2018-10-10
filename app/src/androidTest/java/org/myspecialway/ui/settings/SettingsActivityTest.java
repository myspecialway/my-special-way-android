package org.myspecialway.ui.settings;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myspecialway.R;
import org.myspecialway.ui.utils.BaseClazz;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.myspecialway.ui.utils.Utils.nthChildOf;

/**
 * Created by dr9081 on 10/9/2018.
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest  extends BaseClazz {

    @Rule
    public ActivityTestRule<SettingsActivity> activityTestRule = new ActivityTestRule<>(SettingsActivity.class);


//    @Test
//    public void logout(){
//
//
//            new ActivityTestRule<>(MainScreenActivity.class).launchActivity(null);
//            onView(withId(R.id.settings)).perform(click());
//
//            onView(nthChildOf(withId(android.R.id.list), 2)).perform(click());
//        try {
//            Thread.sleep(1000); //todo find other way to way for element
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        onView(withId(R.id.approveButton)).perform(click());
//
//            System.out.println("asdasdfas");
//
//        }
//student
@Test
    public void verifySettingList(){
    onView(withId(R.id.settings)).perform(click());
    onView(nthChildOf(withId(android.R.id.list), 0)).check(matches(withText("התראות")));
    onView(nthChildOf(withId(android.R.id.list), 0)).check(matches(withText("הדלק או כבה את ההתראות")));

}

}


