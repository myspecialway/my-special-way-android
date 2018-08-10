package org.myspecialway.android.mainscreen;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.myspecialway.android.MswApplication;
import org.myspecialway.android.ScheduleRepository;
import org.myspecialway.android.session.UserSessionManager;

public class MainScreenViewModel extends ViewModel {

    private ScheduleRepository scheduleRepository;
    private UserSessionManager userSessionManager = MswApplication.getInstance().getUserSessionManager();

    public MainScreenViewModel() {

    }

    public void setRepos(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }

    public String getUsername() {

        return userSessionManager.getUserSession().getUserData().username;
    }

    public String getUserAvatar() {

        return "";
    }

    public LiveData<String> getCurrentScheduleName() {

        return scheduleRepository.getScheduleData().getCurrentScheduleName();
    }
}
