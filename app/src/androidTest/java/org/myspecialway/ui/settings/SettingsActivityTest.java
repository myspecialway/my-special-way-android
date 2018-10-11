package org.myspecialway.ui.settings;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myspecialway.R;
import org.myspecialway.ui.mainScreen.MainScreenActivity;
import org.myspecialway.ui.utils.BaseClazz;

import java.util.LinkedList;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

/**
 * Created by dr9081 on 10/9/2018.
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest extends BaseClazz {

    @Rule
    public ActivityTestRule<MainScreenActivity> activityTestRule = new ActivityTestRule<>(MainScreenActivity.class);


    private List<Setting> settings = new LinkedList<>();

    @Before
    public void doBeforeTest() {
        initSettingList(); // todo before each test or at the beginning of the file?
        onView(withId(R.id.settings)).perform(click());
    }

    @Test
    public void verifySettingList() {




        for (int i = 0; i < settings.size(); i++) {
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(i).onChildView(withId(android.R.id.title)).check((matches(withText(settings.get(i).getTitle()))));
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(i).onChildView(withId(android.R.id.summary)).check((matches(withText(settings.get(i).getSummary()))));

            if (settings.get(i).getSwitchType() == "switch") {
                checkSwitchState(settings.get(i).isDefaultSwitchState(), i);
            }
        }

    }


    @Test
    public void changeDefaultStateAlert(){

        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
        checkSwitchState(false, 0);
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());

    }


    @Test
    public void changeDefaultStateSound(){
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(1).perform(click());
        checkSwitchState(true, 0);
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(1).perform(click());

    }


    @Test
    public void logoutDialog(){
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(2).perform(click());
        onView(withId(R.id.approveButton)).check(matches(not(doesNotExist())));
        onView(withId(R.id.cancelButton)).check(matches(not(doesNotExist())));
        onView(withId(R.id.cancelButton)).perform(click());

    }

    public void checkSwitchState(boolean expectedState, int location) {

        if (expectedState) {
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(location).onChildView(withId(android.R.id.switch_widget)).check(matches(isChecked()));
        } else {
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(location).onChildView(withId(android.R.id.switch_widget)).check(matches(not(isChecked())));

        }

    }


    /**
     * @dr9081 init setting list
     * todo find a way to take from other resource
     */

    public void initSettingList() {
        this.settings.add(new Setting("התראות", "הדלק או כבה את ההתראות", "switch", true));
        this.settings.add(new Setting("סאונד", "הדלק או כבה את הניווט הקולי", "switch", false));
        this.settings.add(new Setting("יציאה מהמערכת", "לחץ כדי לצאת מהמערכת", "no", false));
    }


    /**
     * @dr9081 object to represent current settings
     */

    public class Setting {

        private String title;
        private String summary;
        private String switchType;
        private boolean defaultSwitchState;


        public Setting(String title, String summary, String switchType, boolean defaultSwitchState) {
            this.title = title;
            this.summary = summary;
            this.switchType = switchType;
            this.defaultSwitchState = defaultSwitchState;

        }

        public String getTitle() {
            return title;
        }

        public String getSummary() {
            return summary;
        }

        public String getSwitchType() {
            return switchType;
        }


        public boolean isDefaultSwitchState() {
            return defaultSwitchState;
        }
    }

}


