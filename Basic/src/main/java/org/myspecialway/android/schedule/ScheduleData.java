package org.myspecialway.android.schedule;

import android.arch.lifecycle.MutableLiveData;

public class ScheduleData {
    MutableLiveData<String> currentScheduleName = new MutableLiveData<>();

    public ScheduleData(String newScheduleName) {
        this.currentScheduleName.setValue(newScheduleName);
    }

    public MutableLiveData<String> getCurrentScheduleName() {
        return currentScheduleName;
    }
}
