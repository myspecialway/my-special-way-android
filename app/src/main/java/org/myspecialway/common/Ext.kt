package org.myspecialway.common

import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.reactivex.Flowable
import io.reactivex.Observable
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

fun <T> Flowable<T>.filterAtError(): Flowable<T> = materialize()
        .map {  it }
        .filter { !it.isOnError }.dematerialize<T>()

fun Long.roundSeconds(): Long {
    val date = Date()
    date.time = this
    val cal = Calendar.getInstance()
    cal.time = date
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}