package org.myspecialway.ui.notifications

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import io.reactivex.Observable
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

        val (notificationTitle, current, previous) = getBundle(intent)

        // control if the navigate button will be shown
        navigationButton.visibility = if (current.unityDest == previous.unityDest || current.isLast) {
            View.GONE
        } else {
            View.VISIBLE
        }

        notificationText.text = notificationTitle
        image.load(current.image ?: "")
        navigationButton.setOnClickListener {

            Navigation.toUnityNavigation(this, "C1")
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

    private fun getBundle(intent: Intent): Triple<String, ScheduleRenderModel, ScheduleRenderModel> {
        val notificationTitle = intent.getStringExtra(NOTIFICATION_TITLE)
        val current = intent.getParcelableExtra<ScheduleRenderModel>(SCHEDULE_CURRENT_KEY)
        val previous = intent.getParcelableExtra<ScheduleRenderModel>(SCHEDULE_PREVIOUS_KEY)
        return Triple(notificationTitle, current, previous)
    }

    companion object {
        const val SCHEDULE_CURRENT_KEY = "schedule_current_key"
        const val SCHEDULE_PREVIOUS_KEY = "schedule_previous_key"
        const val NOTIFICATION_TITLE = "notification_title"
    }
}