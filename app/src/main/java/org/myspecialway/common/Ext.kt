package org.myspecialway.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.preference.PreferenceManager
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

import io.reactivex.Flowable
import org.myspecialway.R

import org.myspecialway.ui.login.LoginActivity
import java.util.*
import org.myspecialway.R.id.imageView

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

fun Context.logout() {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    sharedPreferences.edit().clear().apply()
    // clear sp, navigate login page with clear top flag
    val intent = Intent(this, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK

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
