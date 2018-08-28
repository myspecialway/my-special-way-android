package org.myspecialway

import android.app.Application
import android.content.Context
import org.koin.android.ext.android.startKoin
import org.myspecialway.di.mySpecialWay
import org.myspecialway.session.UserSessionManager
import org.myspecialway.ui.login.gateway.LoginGateway
import org.myspecialway.utils.TokenParser
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    var userSessionManager: UserSessionManager? = null
        private set

    override fun onCreate() {
        super.onCreate()

        startKoin(this, mySpecialWay)

        instance = this

        initAppComponents()
    }


    private fun initAppComponents() {

        val retrofitService = createRetrofitService()
        userSessionManager = createUserSessionManager(retrofitService)

    }

    private fun createRetrofitService(): Retrofit {

        return Retrofit.Builder().baseUrl(getString(R.string.baseUrl)).addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun createUserSessionManager(retrofit: Retrofit): UserSessionManager {

        val loginGateway = LoginGateway(retrofit)

        return UserSessionManager(loginGateway, TokenParser(), getSharedPreferences("creds", Context.MODE_PRIVATE))
    }



    companion object {

        var instance: App? = null
            private set
    }
}
