package org.myspecialway;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import org.myspecialway.android.ListExamplesActivity;
import org.myspecialway.android.R;

public class Notifications {

    NotificationCompat.Builder mBuilder;
    Context context;
    public static final String CHANNEL_ID = "47ry65t";
    private int NOTIFICATIONID = 1452673;

    public Notifications(Context cont){
        context = cont;
    }

    /**
     * Show a notification to the user
     * @param title
     * @param text
     */
    public void showNotification(String title, String text){
        Notification notification = createNotification(title, text);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(NOTIFICATIONID, notification);
    }

    /**
     * Show a navigation notification to the user
     * @param navigateTo
     */
    public void showNavigationNotification(ClassDetails navigateTo) {
        String userName = getUserName();
        String title = context.getString(R.string.notification_navigation_title, navigateTo.getClassName());
        String text = context.getString(R.string.notification_navigation_text, navigateTo.getClassName(), userName);
        showNotification(title, text);

    }

    private String getUserName() {
        //TODO - get the user private name to show in the notificaiton. Get the name from login
        return "אלי";
    }

    private Notification createNotification(String title, String text){

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, ListExamplesActivity.class); // TODO - need to start navigation
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setVisibility(Notification.VISIBILITY_PUBLIC);

        return mBuilder.build();
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.channel_name);
            String description = context.getResources().getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(Notifications.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            android.app.NotificationManager notificationManager = context.getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
