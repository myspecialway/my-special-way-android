package org.myspecialway

import android.app.Application
import android.content.Context
import org.koin.android.ext.android.startKoin
import org.myspecialway.di.OnMyWayApp

import org.myspecialway.ui.login.gateway.ILoginGateway
import org.myspecialway.ui.login.gateway.LoginGateway
import org.myspecialway.ui.main.ScheduleRepository
import org.myspecialway.schedule.gateway.ScheduleGateway
import org.myspecialway.session.UserSessionManager
import org.myspecialway.utils.JWTParser

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    var userSessionManager: UserSessionManager? = null
        private set
    var scheduleRepository: ScheduleRepository? = null
        private set

    override fun onCreate() {
        super.onCreate()

        startKoin(this, OnMyWayApp)

        instance = this

        initAppComponents()
    }

    private fun initAppComponents() {

        val retrofitService = createRetrofitService()
        userSessionManager = createUserSessionManager(retrofitService)
        scheduleRepository = createScheduleRepository(retrofitService)
    }

    private fun createRetrofitService(): Retrofit {

        return Retrofit.Builder().baseUrl(getString(R.string.baseUrl)).addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun createUserSessionManager(retrofit: Retrofit): UserSessionManager {

        val loginGateway = LoginGateway(retrofit)

        return UserSessionManager(loginGateway, JWTParser(), getSharedPreferences("creds", Context.MODE_PRIVATE))
    }


    private fun createScheduleRepository(retrofit: Retrofit): ScheduleRepository {

        return ScheduleRepository(ScheduleGateway(retrofit))
    }

    companion object {

        var instance: App? = null
            private set
    }
}
