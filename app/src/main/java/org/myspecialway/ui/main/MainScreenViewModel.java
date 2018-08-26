package org.myspecialway.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.myspecialway.App;
import org.myspecialway.session.UserSessionManager;

public class MainScreenViewModel extends ViewModel {

    private ScheduleRepository scheduleRepository = App.Companion.getInstance().getScheduleRepository();
    private UserSessionManager userSessionManager = App.Companion.getInstance().getUserSessionManager();

    public MainScreenViewModel() {

    }

    public String getUsername() {

        return userSessionManager.getUserData().firstname + " " + userSessionManager.getUserData().lastname;
    }

    public String getUserAvatar() {

        return "";
    }

    public LiveData<String> getCurrentScheduleName() {

        return scheduleRepository.getScheduleData().getCurrentScheduleName();
    }
}
