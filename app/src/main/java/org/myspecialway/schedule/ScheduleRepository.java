package org.myspecialway.schedule;

import org.myspecialway.ui.login.RequestCallback;
import org.myspecialway.schedule.gateway.IScheduleGateway;
import org.myspecialway.schedule.gateway.ScheduleResponse;

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
