package org.myspecialway;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.myspecialway.android.MswApplication;
import org.myspecialway.android.login.LoginActivity;
import org.myspecialway.android.mainscreen.MainScreenActivity;

public class SplashActivity extends AppCompatActivity {

    private final String TAG = SplashActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Notifications.createNotificationChannel(this);

        if(MswApplication.getInstance().getUserSessionManager().isLoggedIn()){
            startMainActivity();
        }
        else{
            startLoginActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMainActivity();
    }


    private void startMainActivity() {
        if(checkGooglePlayServices()){
            Intent intent = new Intent(this, MainScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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
