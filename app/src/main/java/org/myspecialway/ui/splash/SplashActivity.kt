package org.myspecialway.ui.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.myspecialway.App
import org.myspecialway.common.Navigation
import org.myspecialway.session.UserSession
import org.myspecialway.ui.login.RequestCallback

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (App.instance?.userSessionManager!!.isLoggedIn) {

            Navigation.toMainActivity(this@SplashActivity)

        } else {

            App.instance?.userSessionManager?.refreshSessionIfNeeded(object : RequestCallback<UserSession> {

                override fun onSuccess(result: UserSession) {
                    Navigation.toMainActivity(this@SplashActivity)
                }

                override fun onFailure(t: Throwable) {
                    Navigation.toLoginActivity(this@SplashActivity)
                }
            })
        }
    }
}
