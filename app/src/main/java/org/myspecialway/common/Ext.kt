package org.myspecialway.common

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.reactivex.Flowable
import io.reactivex.Observable
import org.myspecialway.ui.login.LoginActivity
import java.util.*


// use this to avoid layout inflater boilerplate
fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
        LayoutInflater.from(context).inflate(layoutRes, this, false)

// load resource files with Glide
fun ImageView.loadFromRes(url: String) =
        Glide.with(this.context.applicationContext)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)

// show snackBar
fun snackBar(view: View, message: String) = Snackbar
        .make(view, message, Snackbar.LENGTH_SHORT)
        .apply { show() }

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG)

fun <T> Flowable<T>.filterAtError(): Flowable<T> = materialize()
        .map {  it }
        .filter { !it.isOnError }.dematerialize<T>()

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
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    this.startActivity(intent)
}