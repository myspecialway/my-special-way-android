package org.myspecialway.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMMessagingService extends FirebaseMessagingService {
    private final String TAG = FCMMessagingService.this.getClass().getSimpleName();

    public FCMMessagingService() {
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            /* Check if data needs to be processed by long running job */
            // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            // OR Handle message within 10 seconds

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // TODO - pass msg to notification service
    }
}
