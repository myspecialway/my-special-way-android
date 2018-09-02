package org.myspecialway

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.myspecialway.di.mySpecialWay


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, mySpecialWay)
        instance = this
    }

    companion object {
        var instance: App? = null
            private set
    }
}
