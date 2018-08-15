package org.myspecialway;

import android.app.Application;
import android.content.Context;

import org.myspecialway.login.gateway.ILoginGateway;
import org.myspecialway.login.gateway.LoginGateway;
import org.myspecialway.mainmenu.ScheduleRepository;
import org.myspecialway.schedule.gateway.IScheduleGateway;
import org.myspecialway.schedule.gateway.ScheduleGateway;
import org.myspecialway.session.UserSessionManager;
import org.myspecialway.utils.JWTParser;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MswApplication extends Application {

    private UserSessionManager userSessionManager;
    private ScheduleRepository scheduleRepository;

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


    private ScheduleRepository createScheduleRepository(Retrofit retrofit){

        return new ScheduleRepository(new ScheduleGateway(retrofit));
    }

    public UserSessionManager getUserSessionManager(){
        return userSessionManager;
    }

    public ScheduleRepository getScheduleRepository(){return scheduleRepository; }
}
