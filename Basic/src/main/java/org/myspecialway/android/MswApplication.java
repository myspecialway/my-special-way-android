package org.myspecialway.android;

import android.app.Application;
import android.content.Context;

import org.myspecialway.android.login.gateway.ILoginGateway;
import org.myspecialway.android.login.gateway.LoginGateway;
import org.myspecialway.android.schedule.ScheduleRepository;
import org.myspecialway.android.schedule.gateway.IScheduleGateway;
import org.myspecialway.android.schedule.gateway.ScheduleGateway;
import org.myspecialway.android.session.UserSessionManager;
import org.myspecialway.android.utils.JWTParser;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MswApplication extends Application {

    private UserSessionManager userSessionManager;
    private ScheduleRepository scheduleRepository;
    private IScheduleGateway scheduleGateway;

    private static MswApplication instance;

    public static MswApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initAppComponents();
    }

    private void initAppComponents(){

        Retrofit retrofitService = createRetrofitService();
        userSessionManager = createUserSessionManager(retrofitService);
        scheduleRepository = createScheduleRepository(retrofitService);
    }

    private Retrofit createRetrofitService(){

        return new Retrofit.Builder().baseUrl(getString(R.string.baseUrl)).addConverterFactory(GsonConverterFactory.create()).build();
    }

    private UserSessionManager createUserSessionManager(Retrofit retrofit){

        ILoginGateway loginGateway = new LoginGateway(retrofit);

        return new UserSessionManager(loginGateway, new JWTParser(), getSharedPreferences("creds", Context.MODE_PRIVATE));
    }

    public UserSessionManager getUserSessionManager(){
        return userSessionManager;
    }

    private ScheduleRepository createScheduleRepository(Retrofit retrofit){

        scheduleGateway = new ScheduleGateway(retrofit);
        return new ScheduleRepository(scheduleGateway);
    }

    public ScheduleRepository getScheduleRepository(){return scheduleRepository; }

    public IScheduleGateway getScheduleGateway() {
        return scheduleGateway;
    }
}
