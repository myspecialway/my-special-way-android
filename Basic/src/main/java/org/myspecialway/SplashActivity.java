package org.myspecialway;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.myspecialway.android.ListExamplesActivity;

public class SplashActivity extends AppCompatActivity {

    private final String TAG = SplashActivity.this.getClass().getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        startMainActivity();
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(Notifications.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            android.app.NotificationManager notificationManager = getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        startMainActivity();
    }


    private void startMainActivity() {
        if(checkGooglePlayServices()){
            Intent intent = new Intent(this, ListExamplesActivity.class);
            startActivity(intent);
            finish();
        }
    }



    private boolean checkGooglePlayServices() {

        int googlePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (googlePlayServicesAvailable != ConnectionResult.SUCCESS) {
            Log.e(TAG, "Need to update play services");
            // ask user to update google play services.
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, googlePlayServicesAvailable, 1);            dialog.show();
            return false;
        } else {
            // google play services is updated.
            //your code goes here...
            return true;
        }
    }
}
