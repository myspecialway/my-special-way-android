package org.myspecialway.ui.mainScreen;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myspecialway.R;
import org.myspecialway.ui.utils.BaseClazz;
import org.myspecialway.ui.utils.UserEntity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by dr9081 on 10/8/2018.
 */
@RunWith(AndroidJUnit4.class)
public class MainScreenActivityTest extends BaseClazz {

    UserEntity ue = new UserEntity();

    @Rule
    public  ActivityTestRule<MainScreenActivity> activityTestRule = new ActivityTestRule<>(MainScreenActivity.class);





    /**
     * verify existence os elements
     */


    @Test
    public void settingsButtonDefault() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.settings)).check(matches(isDisplayed()));
        onView(withId(R.id.settings)).check(matches(isClickable()));
        onView(withId(R.id.settings)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.settings)).check(matches(not(isSelected())));

    }


    @Test
    public void clickSettings() {
        onView(withId(R.id.settings)).perform(click());
        try {
            onView(withId(R.id.dayScheduleTitle)).check(matches(withText("הגדרות")));
            Espresso.pressBack();
        } catch (Exception e) {

            System.out.println("is there an ex");
        }


    }

    @Test
    public void studentNameAsInDB() {
        onView(withId(R.id.userDisplayName)).check(matches(withText(ue.getUserName())));

    }

//    @Test
//    public void studentNameColor() {
//        onView(withId(R.id.userDisplayName)).check(matches(hasTextColor(-16777216)));
//    }


    @Test

    public void wcNavButton() {

        onView(withId(R.id.wc_nav_button)).check(matches(isDisplayed()));
        onView(withId(R.id.wc_nav_button)).check(matches(isClickable()));

    }

    @Test

    public void scheduleButton() {

        onView(withId(R.id.scheduleButton)).check(matches(isDisplayed()));
        onView(withId(R.id.scheduleButton)).check(matches(isClickable()));

    }

    @Test

    public void navButton() {

        onView(withId(R.id.navButton)).check(matches(isDisplayed()));
        onView(withId(R.id.navButton)).check(matches(isClickable()));

    }


    @Test

    public void userAvatarDisplayed() {

        onView(withId(R.id.user_avatar_image)).check(matches(not(doesNotExist())));
        onView(withId(R.id.user_avatar_image)).check(matches(not(isClickable())));

    }

    @Test
// todo check property
    public void verifyCurrentStatusDisplayed() {

        onView(withId(R.id.scheduleName)).check(matches(not(doesNotExist())));


    }


    /**
     * current status data is displayed according to correct current hour
     */
    @Test

    public void verifyCurrentStatusData() {

        // todo take current status from list
        onView(withId(R.id.scheduleName)).check(matches(not(doesNotExist())));
        onView(withId(R.id.scheduleName)).check(matches(not(isClickable())));

    }



}
