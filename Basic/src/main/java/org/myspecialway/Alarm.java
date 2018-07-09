package org.myspecialway;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Alarm {
    AlarmManager am;
    Context context;
    PendingIntent pendingIntent;
    private static Alarm alarmInstance = null;

    public static Alarm getInstance(Context cont){
        if (alarmInstance == null){
            alarmInstance = new Alarm(cont);
        }
        return alarmInstance;
    }
    private Alarm(Context cont){
        context = cont;
        am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }


    /**
     * Schedule a new alarm x seconds from now.
     * @param secondsFromNow
     */
    public void scheduleAlarm(int secondsFromNow){
        Intent intent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        long triggerAtMillis = System.currentTimeMillis() + (secondsFromNow * 1000);
        Log.d("Peleg", String.valueOf(triggerAtMillis));
        am.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        Log.d("Peleg", "Alarm scheduled");
    }

    /**
     * Cancel an existing alarm, if any
     */
    public void cancelAlarm(){
        if(pendingIntent != null) {
            am.cancel(pendingIntent);
        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        public AlarmReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Peleg", "Alarm fired");
            Notifications nm = new Notifications(context);
            ClassDetails cd = new ClassDetails("32","34","מדעים", "1");
            nm.showNavigationNotification(cd);
        }
    }


}
