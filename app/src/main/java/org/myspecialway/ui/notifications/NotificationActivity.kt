package org.myspecialway.ui.notifications

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_notification.*
import org.myspecialway.R
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.ScheduleRenderModel
import java.util.concurrent.TimeUnit


class NotificationActivity : Activity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        val (notificationTitle, destination) = getBundle(intent)


        disposable = Observable.interval(0, 10, TimeUnit.MINUTES)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    finish()
                }, { })

        notificationText.text = "מולדת" //TODO-pass the real class name //notificationTitle
        navigationButton.setOnClickListener {
            // navigation dest code should come from backend?
//            Navigation.toUnityNavigation(this, destination)
            Navigation.toUnityNavigation(this, "C1")
            finish()
        }
    }

    private fun getBundle(intent: Intent): Pair<String, ScheduleRenderModel> {
        val notificationTitle = intent.getStringExtra(NOTIFICATION_PAYLOAD)
        val destination = intent.getParcelableExtra<ScheduleRenderModel>(SCHEDULE_KEY)
        return Pair(notificationTitle, destination)
    }

    companion object {
        const val SCHEDULE_KEY = "schedule_key"
        const val NOTIFICATION_PAYLOAD = "notification_title"
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}