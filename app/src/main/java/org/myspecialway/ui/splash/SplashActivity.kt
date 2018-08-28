package org.myspecialway.ui.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.myspecialway.App
import org.myspecialway.common.Navigation
import org.myspecialway.session.SessionManager
import org.myspecialway.session.UserSession
import org.myspecialway.ui.login.LoginRepository
import org.myspecialway.ui.login.gateway.RequestCallback

class SplashActivity : AppCompatActivity() {

    private val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when(sessionManager.isLoggedIn) {
            true -> Navigation.toMainActivity(this@SplashActivity)
            false -> Navigation.toLoginActivity(this@SplashActivity)
        }
    }
}
