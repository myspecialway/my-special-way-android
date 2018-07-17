package org.myspecialway.android.mainscreen;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.myspecialway.android.ScheduleRepository;
import org.myspecialway.android.UserDataRepository;

public class MainScreenViewModel extends ViewModel {

    UserDataRepository userDataRepository;
    ScheduleRepository scheduleRepository;



    public MainScreenViewModel() {

    }

    public void setRepos(UserDataRepository userDataRepository, ScheduleRepository scheduleRepository){
        this.userDataRepository = userDataRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public LiveData<String> getUserName() {

         return userDataRepository.getUserData().getUserName();
    }


    public LiveData<String> getUserAvatar() {

        return userDataRepository.getUserData().getUserAvatar();
    }

    public LiveData<String> getCurrentScheduleName() {

        return scheduleRepository.getScheduleData().getCurrentScheduleName();
    }
}
