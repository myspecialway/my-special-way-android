package org.myspecialway.mainmenu;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.myspecialway.MswApplication;
import org.myspecialway.session.UserSessionManager;

public class MainScreenViewModel extends ViewModel {

    private ScheduleRepository scheduleRepository = MswApplication.getInstance().getScheduleRepository();
    private UserSessionManager userSessionManager = MswApplication.getInstance().getUserSessionManager();

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
