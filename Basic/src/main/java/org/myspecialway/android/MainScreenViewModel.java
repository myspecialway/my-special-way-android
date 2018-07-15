package org.myspecialway.android;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.drawable.Drawable;

public class MainScreenViewModel extends ViewModel {

    private MutableLiveData<String> userName;
    private MutableLiveData<Drawable> userAvatar;
    private MutableLiveData<String> currentSheduleName;

    public MutableLiveData<String> getUserName() {

        if (userName==null){
            retrieveUserName();
        }
        return userName;
    }

    private void retrieveUserName() {
    }
    public MutableLiveData<Drawable> getUserAvatar() {
        if (userAvatar==null){
            retrieveUserAvatar();
        }
        return userAvatar;
    }

    private void retrieveUserAvatar() {
    }

    public MutableLiveData<String> getCurrentSheduleName() {
        if(currentSheduleName==null){
            retrieveCurrentScheduleName();
        }
        return currentSheduleName;
    }

    private void retrieveCurrentScheduleName() {
    }



}
