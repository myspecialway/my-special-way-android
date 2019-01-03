package org.myspecialway

import android.app.Application
import com.evernote.android.job.JobManager
import org.koin.android.ext.android.startKoin
import com.squareup.picasso.Picasso
import com.jakewharton.picasso.OkHttp3Downloader
import mySpecialWay
import org.myspecialway.ui.alarms.JobCreator

class App : Application() {

    companion object {
        var instance: App? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, mySpecialWay)
        instance = this
        configurePicassoOffline()
        JobManager.create(this).addJobCreator(JobCreator())
    }

    private fun configurePicassoOffline() {
        val builder = Picasso.Builder(this)
        builder.downloader(OkHttp3Downloader(this, Integer.MAX_VALUE.toLong()))
        val built = builder.build()
        built.isLoggingEnabled = true
        Picasso.setSingletonInstance(built)
    }
}