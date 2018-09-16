package org.myspecialway.common

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import io.reactivex.Observable


class KeyboardManager(private val activity: Activity) {

    fun status(): Observable<KeyboardStatus> = Observable.create<KeyboardStatus> { emitter ->
        val rootView = activity.findViewById<View>(android.R.id.content)

        val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {

            val rect = Rect().apply { rootView.getWindowVisibleDisplayFrame(this) }

            val screenHeight = rootView.height

            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                emitter.onNext(KeyboardStatus.OPEN)
            } else {
                emitter.onNext(KeyboardStatus.CLOSED)
            }
        }

        rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

        emitter.setCancellable {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
        }

    }.distinctUntilChanged()
}

enum class KeyboardStatus {
    OPEN, CLOSED
}