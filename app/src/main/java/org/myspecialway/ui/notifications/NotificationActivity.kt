package org.myspecialway.ui.notifications

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_notification.*

import org.myspecialway.R
import org.myspecialway.common.Navigation
import org.myspecialway.common.load
import org.myspecialway.ui.agenda.ScheduleRenderModel
import java.util.concurrent.TimeUnit


class NotificationActivity : Activity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        selfDestroyTimer()
        val (notificationTitle, currentSchedule) = getBundle(intent)
        notificationText.text = notificationTitle
        image.load(currentSchedule.image ?: "")
        navigationButton.setOnClickListener {
            Navigation.toUnityNavigation(this, currentSchedule.unityDest)
            finish()
        }
    }

    private fun selfDestroyTimer() {
        disposable = Observable.interval(10, TimeUnit.MINUTES)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun getBundle(intent: Intent): Pair<String, ScheduleRenderModel> {
        val notificationTitle = intent.getStringExtra(NOTIFICATION_TITLE)
        val destination = intent.getParcelableExtra<ScheduleRenderModel>(SCHEDULE_KEY)
        return Pair(notificationTitle, destination)
    }

    companion object {
        const val SCHEDULE_KEY = "schedule_key"
        const val NOTIFICATION_TITLE = "notification_title"
    }
}