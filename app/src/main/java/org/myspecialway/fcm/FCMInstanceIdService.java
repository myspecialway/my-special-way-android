package org.myspecialway.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.myspecialway.utils.Logger;

public class FCMInstanceIdService extends FirebaseInstanceIdService {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.Companion.d(TAG, "Refreshed token: " + refreshedToken);

        if(refreshedToken == null){
            //TODO - Handle
        }

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {

        //TODO

    }
}
