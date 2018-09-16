package org.myspecialway.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import org.myspecialway.App

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {

    val composite: CompositeDisposable? = CompositeDisposable()

    abstract fun render()


}