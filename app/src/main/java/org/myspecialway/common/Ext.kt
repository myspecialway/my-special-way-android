package org.myspecialway.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import com.evernote.android.job.DailyJob
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Flowable
import org.myspecialway.R
import org.myspecialway.ui.agenda.AgendaIndex
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.alarms.JobCreator
import org.myspecialway.ui.login.LoginActivity
import java.util.*
import java.util.concurrent.TimeUnit

// use this to avoid layout inflater boilerplate
fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
        LayoutInflater.from(context).inflate(layoutRes, this, false)

// load resource files with Glide
fun ImageView.load(url: Int) =
        Picasso.with(this.context.applicationContext)
                .load(url)
                .error(R.drawable.reading)
                .into(this)


fun ImageView.load(url: String) =
        Picasso.with(this.context.applicationContext)
                .load(url)
                .error(R.drawable.reading)
                .into(this)


fun <T> Flowable<T>.filterAtError(): Flowable<T> = materialize()
        .filter { !it.isOnError }
        .dematerialize<T>()

fun Date.addHour(hours: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.HOUR_OF_DAY, hours)
    return cal.time
}

fun Context.logout() {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    sharedPreferences.edit().clear().apply()
    // clear sp, navigate login page with clear top flag
    val intent = Intent(this, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    this.startActivity(intent)
}

fun Int.dpToPixels(context: Context) = (this * context.resources.displayMetrics.density + 0.5f).toInt()

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun View.animateY(y: Float) =
        animate()
                .translationY(y)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()

fun Button.enable(enable: Boolean) = when (enable) {
    true -> {
        this.isEnabled = true
    }
    false -> {
        this.isEnabled = false
    }
}

fun Any.toJson(): String = Gson().toJson(this)


fun MutableList<ScheduleRenderModel>.filterTodayList() =
        asSequence()
                .filter { AgendaIndex.todayWeekIndex(Calendar.getInstance()) == it.time?.dayDisplay }
                .sortedBy {
                    it.index?.substringBefore("_")?.toInt()
                }
                .distinctBy { it.index }
                .toList()


fun MutableList<ScheduleRenderModel>.getRemainingAlarmsForToday() =
        asSequence()
                .filter { AgendaIndex.todayWeekIndex(Calendar.getInstance()) == it.time?.dayDisplay }
                .sortedBy { it.index?.substringBefore("_")?.toInt() }
                .distinctBy { it.index }
                .toList()
                .filter { System.currentTimeMillis() < it.time!!.date.time }

fun Context.handleBatteryManagement() {
    val intent = Intent()
    val packageName = packageName
    val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
    if (!pm.isIgnoringBatteryOptimizations(packageName)) {
        intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }
}
