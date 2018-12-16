package org.myspecialway.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Flowable
import org.myspecialway.R
import org.myspecialway.ui.agenda.AgendaIndex
import org.myspecialway.ui.agenda.ReminderRenderModel
import org.myspecialway.ui.agenda.ReminderType
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.login.LoginActivity
import java.util.*

const val SUN_TILL_THU = 6

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
                .sortedBy {  it.index?.substringBefore("_")?.toInt() }
                .distinctBy { it.index }
                .toList()
                .filter { System.currentTimeMillis() < it.time!!.date.time  }

fun MutableList<ReminderRenderModel>.getRemindersForToday(): MutableList<Pair<Long, ReminderType>> {
    val reminders : MutableList<Pair<Long, ReminderType>> = mutableListOf()
    // days index coming from server are zero based, and 6 is sun-Thu
    val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) -1
    forEach {
        if (it.enabled) {
            val reminderType = it.type
            it.reminderTime.forEach {

                if (it.daysindex.contains(dayOfWeek) || (it.daysindex.contains(SUN_TILL_THU) && dayOfWeek < 5)) {
                    it.hours.forEach {
                        val millisToReminderTime = getMillisToReminderTime(it)
                        if (millisToReminderTime >= 0) reminders.add(Pair(millisToReminderTime, reminderType))
                    }
                }
            }
        }
    }
    return reminders
}

/**
 * @param reminderHourStr  string of reminder time in format "08:30"
 * @return milliseconds till that time, or -1 if invalid.
 */
private fun getMillisToReminderTime(reminderHourStr: String): Long {
    val hour = reminderHourStr.substringBefore(':', reminderHourStr)
    val minutes = reminderHourStr.substringAfter(':', "00")
    try {
        if (!hour.isEmpty()) {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, hour.toInt())
            cal.set(Calendar.MINUTE, minutes.toInt())

            return cal.timeInMillis - System.currentTimeMillis()
        }
    } catch (e: Exception) {
        return -1
    }
    return -1
}
