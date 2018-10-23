package org.myspecialway.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.myspecialway.App

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {

    val composite: CompositeDisposable? = CompositeDisposable()

    var disposable: Disposable? = null

    abstract fun render()

    override fun onDestroy() {
        super.onDestroy()
        composite?.dispose()
        disposable?.dispose()
    }
}