package org.myspecialway.common

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.support.annotation.LayoutRes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import com.google.gson.Gson
import com.squareup.picasso.Picasso

import io.reactivex.Flowable
import org.myspecialway.R

import org.myspecialway.ui.login.LoginActivity
import java.util.*
import org.myspecialway.data.local.Database
import org.myspecialway.di.LocalProperties.DB_NAME

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
        .map {
            it
        }
        .filter { !it.isOnError }
        .dematerialize<T>()

fun Date.addHour(hours: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.HOUR_OF_DAY, hours)
    return cal.time
}

fun Date.addMinutes(minutes: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.MINUTE, minutes)
    return cal.time
}

fun Context.logout() {
    PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply()
    nukeSchedule()

    val intent = Intent(this, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    this.startActivity(intent)
}

private fun Context.nukeSchedule() = Room.databaseBuilder(this, Database::class.java, DB_NAME)
        .allowMainThreadQueries()
        .build()
        .localDataSourceDAO()
        .nukeSchedule()


fun Int.dpToPixels(context: Context) = (this * context.resources.displayMetrics.density + 0.5f).toInt()

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Button.enable(enable: Boolean) = when (enable) {
    true -> {
        this.isEnabled = true
    }
    false -> {
        this.isEnabled = false
    }
}

fun Any.toJson(): String = Gson().toJson(this)
