package org.myspecialway.android;

import org.myspecialway.android.schedule.ScheduleData;

public class ScheduleRepository {

    ScheduleData scheduleData = new ScheduleData("שיעור חשבון");

//    public ScheduleRepository(ScheduleData scheduleData) {
//        this.scheduleData = scheduleData;
//    }

    public ScheduleData getScheduleData() {
        return scheduleData;
    }
}
