package org.myspecialway.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.preference.PreferenceManager
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.Flowable
import org.myspecialway.ui.agenda.*
import org.myspecialway.R
import org.myspecialway.ui.alarms.AlarmJob
import org.myspecialway.ui.login.LoginActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

const val TAG = "Ext"
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

fun Context.downloadImage(url: Int, imageName: String) =
        Picasso.with(this.applicationContext)
                .load(url)
                .into(saveImageToFile(this.applicationContext, imageName))

fun Context.downloadImage(url: String, imageName: String) =
        Picasso.with(this.applicationContext)
                .load(url)
                .into(saveImageToFile(this.applicationContext, imageName))


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
    AlarmJob.cancelAllJobs() // to avoid more notifications.
    //TODO: delete all from local storage
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


fun MutableList<ScheduleRenderModel>.getRemainingAlarmsForToday(nonActiveTimes: List<NonActiveTimeRenderModel>) =
        asSequence()
                .filter { AgendaIndex.todayWeekIndex(Calendar.getInstance()) == it.time?.dayDisplay }
                .sortedBy { it.index?.substringBefore("_")?.toInt() }
                .distinctBy { it.index }
                .toList()
                .filter { System.currentTimeMillis() < it.time!!.date.time && !nonActiveTimes.isEventInsideNonActiveTime(it.time!!.date.time) }

fun MutableList<ReminderRenderModel>.getRemindersForToday(nonActiveTimes: List<NonActiveTimeRenderModel>): MutableList<Pair<Long, ReminderType>> {
    var reminders: MutableList<Pair<Long, ReminderType>> = mutableListOf()
    // days index coming from server are zero based, and 6 is sun-Thu
    val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
    forEach {
        if (it.enabled) {
            val reminderType = it.type
            it.reminderTime.forEach {

                if (it.daysindex.contains(dayOfWeek) || (it.daysindex.contains(SUN_TILL_THU) && dayOfWeek < 5)) {
                    it.hours.forEach {
                        val reminderTime = getReminderTime(it)
                        val reminderTimeHasNotPassed = reminderTime > 0 && reminderTime - System.currentTimeMillis() >= 0
                        if (reminderTimeHasNotPassed && !nonActiveTimes.isEventInsideNonActiveTime(reminderTime)) reminders.add(Pair(reminderTime, reminderType))
                    }
                }
            }
        }
    }

    reminders = reminders.distinct().toMutableList()
    return reminders
}

fun List<NonActiveTimeRenderModel>.isEventInsideNonActiveTime(eventStartTime: Long): Boolean {
    forEach {
        val eventDate = Date(eventStartTime)
        if (it.startDateTime?.before(eventDate) == true && it.endDateTime?.after(eventDate) == true) {
//            Logger.d(TAG, "time $eventDate overlaps in non active time '${it.title}'. (${it.startDateTime} -  ${it.endDateTime})")
            return true
        }
    }

    return false
}

/**
 * @param reminderHourStr  string of reminder time in format "08:30"
 * @return reminder time, or -1 if invalid.
 */
private fun getReminderTime(reminderHourStr: String): Long {
    val hour = reminderHourStr.substringBefore(':', reminderHourStr)
    val minutes = reminderHourStr.substringAfter(':', "00")
    try {
        if (!hour.isEmpty()) {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, hour.toInt())
            cal.set(Calendar.MINUTE, minutes.toInt())
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis
        }
    } catch (e: Exception) {
        return -1
    }
    return -1
}

private fun saveImageToFile(context: Context, imageName: String): Target {

    val target = object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

        }

        override fun onBitmapFailed(errorDrawable: Drawable?) {
            Log.d(TAG, "onBitmapFailed " + errorDrawable.toString());
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                Runnable {
                    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "mySpecialWay")

                    if (!file.exists()) {
                        file.mkdir();
                    }
                    val imageFile = File(file, imageName)

                    try {

                        if (!imageFile.exists()) {
                            imageFile.createNewFile();
                        }

                        val ostream = FileOutputStream(imageFile);
                        bitmap?.compress(Bitmap.CompressFormat.PNG, 80, ostream);
                        ostream.flush();
                        ostream.close();
                    } catch (e: IOException) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                }.run()
            }
        }
    }
    return target
}
