package org.myspecialway

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.myspecialway.di.mySpecialWay
import com.squareup.picasso.Picasso
import com.jakewharton.picasso.OkHttp3Downloader




class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, mySpecialWay)
        instance = this

        configurePicassoOffline()
    }

    private fun configurePicassoOffline() {
        val builder = Picasso.Builder(this)
        builder.downloader(OkHttp3Downloader(this, Integer.MAX_VALUE.toLong()))
        val built = builder.build()
        built.isLoggingEnabled = true
        Picasso.setSingletonInstance(built)
    }

    companion object {
        var instance: App? = null
            private set
    }
}
