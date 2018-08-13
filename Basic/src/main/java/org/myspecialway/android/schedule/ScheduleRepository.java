package org.myspecialway.android.schedule;

import org.myspecialway.android.login.RequestCallback;
import org.myspecialway.android.schedule.gateway.IScheduleGateway;
import org.myspecialway.android.schedule.gateway.ScheduleResponse;

import java.util.List;

public class ScheduleRepository {

    ScheduleData scheduleData = new ScheduleData("שיעור חשבון");
    IScheduleGateway scheduleGateway;

    public ScheduleRepository(IScheduleGateway scheduleGateway) {

        this.scheduleGateway = scheduleGateway;

    }

//    public ScheduleRepository(ScheduleData scheduleData) {
//        this.scheduleData = scheduleData;
//    }

    public ScheduleData getScheduleData() {
        return scheduleData;
    }

    public void getSchedule(String scheduleQuery, String userAccessToken, RequestCallback<List<ScheduleResponse.Schedule>> callback){

        scheduleGateway.getUserSchedule(scheduleQuery, userAccessToken, callback);

    }
}
