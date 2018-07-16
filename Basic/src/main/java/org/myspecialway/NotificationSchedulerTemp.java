package org.myspecialway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.myspecialway.android.R;
import org.myspecialway.android.SdkExample;

@SdkExample(description = R.string.example_notification)
public class NotificationSchedulerTemp

        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Alarm a = Alarm.getInstance(this);
        a.scheduleAlarm(4);
        finish();
    }
}
