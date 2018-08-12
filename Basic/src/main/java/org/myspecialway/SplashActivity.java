package org.myspecialway;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.myspecialway.android.MswApplication;
import org.myspecialway.android.login.LoginActivity;
import org.myspecialway.android.login.RequestCallback;
import org.myspecialway.android.mainscreen.MainScreenActivity;
import org.myspecialway.android.session.UserSession;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Notifications.createNotificationChannel(this);

        MswApplication.getInstance().getUserSessionManager().refreshSessionIfNeeded(new RequestCallback<UserSession>() {

            @Override
            public void onSuccess(UserSession result) {
                startMainActivity();
            }

            @Override
            public void onFailure(Throwable t) {
                startLoginActivity();
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
        finish();
    }

    private void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
