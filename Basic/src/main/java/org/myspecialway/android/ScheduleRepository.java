package org.myspecialway.android;

public class ScheduleRepository {

    ScheduleData scheduleData = new ScheduleData("Math class");

//    public ScheduleRepository(ScheduleData scheduleData) {
//        this.scheduleData = scheduleData;
//    }

    public ScheduleData getScheduleData() {
        return scheduleData;
    }
}
