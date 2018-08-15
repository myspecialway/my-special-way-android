package org.myspecialway.schedule.gateway;

import android.support.annotation.NonNull;

import org.myspecialway.login.RequestCallback;

import java.util.List;

public interface IScheduleGateway {

    void getUserSchedule(@NonNull String scheduleQuery, @NonNull String userAccessToken, @NonNull final RequestCallback<List<ScheduleResponse.Schedule>> callback);
}
