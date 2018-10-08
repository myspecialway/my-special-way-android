package org.myspecialway.ui.agenda;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myspecialway.R;
import org.myspecialway.ui.login.LoginActivity;
import org.myspecialway.ui.utils.AgendaDataFrame;
import org.myspecialway.ui.utils.AgendaListCreator;
import org.myspecialway.ui.utils.RecyclerViewItemCountAssertion;
import org.myspecialway.ui.utils.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.myspecialway.ui.utils.Utils.openSchedule;


@RunWith(AndroidJUnit4.class)
public class AgendaActivityTest {
    AgendaListCreator agendaListCreator = new AgendaListCreator();
    LinkedHashMap<Integer, AgendaDataFrame> agendaList =agendaListCreator.generateAgendaList();

    @ClassRule
    public static ActivityTestRule<AgendaActivity> activityTestRule = new ActivityTestRule<>(AgendaActivity.class);


    @BeforeClass
    public static void beforeClass() {


        //todo get from json
//ActivityTestRule<LoginActivity> activityTestRuleP = new ActivityTestRule<>(LoginActivity.class);
        Utils.clearPreferences( activityTestRule.getActivity());

        try {
            Utils.doLogin(new ActivityTestRule <>(LoginActivity.class));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openSchedule();

    }

    /**
     * item in list verification tests
     */

    /**
     * title Agenda appear on the page
     */
    @Test
    public void agendaTitleAppear() {
        onView(withId(R.id.dayScheduleTitle)).check(matches((isDisplayed())));
        onView(withId(R.id.dayScheduleTitle)).check(matches(withText("סדר היום שלי")));
    }


    /**
     * scroll through item titles list (TODO receive from json) currently hard coded
     * verify all list appear on the screen, not using order to the list
     */

    @Test
    public void verifyItemsTitle() {
        for (Map.Entry<Integer, AgendaDataFrame> entry : agendaList.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue().getTitle() + "*******");
            onView(ViewMatchers.withId(R.id.agendaRecyclerView))
                    .perform(RecyclerViewActions.scrollToPosition(entry.getKey()))
                   .check(matches(hasDescendant(withText(entry.getValue().getTitle()))));
        }

    }

    /**
     * scroll through item timeframes list (TODO receive from json) currently hard coded
     * verify all list appear on the screen, not using order to the list
     */
    @Test
    public void verifyItemTimeFrame() {
        for (Map.Entry<Integer, AgendaDataFrame> entry : agendaList.entrySet()) {

            System.out.println(entry.getKey() +"  "+ entry.getValue().getTitle() + "  "+entry.getValue().getTimeFrame());
            onView(ViewMatchers.withId(R.id.agendaRecyclerView))
                    .perform(RecyclerViewActions.scrollToPosition(entry.getKey()))
                    .check(matches(hasDescendant(withText(entry.getValue().getTimeFrame()))));

        }
    }


    /**
     * scroll to item in the list by title
     * get it's time, and compare to have the correct time by input
     */

    @Test
    public void verifyTitleContainsCorrectTimeFrame() {
        for (Map.Entry<Integer, AgendaDataFrame> entry : agendaList.entrySet()) {

            onView(ViewMatchers.withId(R.id.agendaRecyclerView))
                    .perform(RecyclerViewActions.scrollToPosition(entry.getKey()))
                    .check(matches(hasDescendant(withText(entry.getValue().getTitle())))).check(matches(hasDescendant(withText(entry.getValue().getTimeFrame()))));

        }
    }


    //todo how to verify correct image displayed?
//    @Test
//    public void verifyCorrectImageDisplayed() {
//
//
//
//    }



    /**
     * list display and functionality
     */
    @Test
    public void backFromAgenda() {


        try {
            onView(ViewMatchers.withId(R.id.dayScheduleTitle)).check(matches(isDisplayed()));
          } catch (NoMatchingViewException e) {
            Espresso.pressBack();
            openSchedule();
            onView(withId(R.id.dayScheduleTitle)).check(matches((isDisplayed())));
        }



//        onView(ViewMatchers.withId(R.id.scheduleButton)).perform(click());



//...


    }

    @Test
    public void backButtonAppear() {

    }


    /**
     * todo find out how to recognize this image
     */
    @Test
    public void scrollAgendaList() {
        // todo scroll to last item in the list and then verify last recycle view displayed (bus image)

        onView(ViewMatchers.withId(R.id.agendaRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(agendaListCreator.getNumberOfAgendaItems()));



    }

    /**
     * count all items in the recycle view and compare to the size of the agenda list got from the API
     */
    @Test
    public void verifyCorrectListSize(){

        onView(withId(R.id.agendaRecyclerView)).check(new RecyclerViewItemCountAssertion(agendaListCreator.getNumberOfAgendaItems()+1));

    }



}
