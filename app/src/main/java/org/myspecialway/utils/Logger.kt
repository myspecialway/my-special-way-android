package org.myspecialway.utils

import android.util.Log

class Logger {

    companion object {
        fun d (tag: String, msg : String) {
            Log.d(tag, msg)
        }

        fun w (tag: String, msg : String) {
            Log.w(tag, msg)
        }
        fun e (tag: String, msg : String) {
            Log.e(tag, msg)
        }
        fun e (tag: String, msg : String, th : Throwable) {
            Log.e(tag, msg, th)
        }
    }
}