package org.myspecialway.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.login_dialog_error.view.*
import org.myspecialway.R
import org.myspecialway.common.BaseDialog

class LoginErrorDialog(context: Context) : BaseDialog() {
    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.login_dialog_error, null)
    }

    val content: TextView by lazy {
        dialogView.findViewById<TextView>(R.id.content)
    }

    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    fun closeIconClickListener(func: (() -> Unit)? = null) =
            with(dialogView.closeButton) {
                setClickListenerToDialogIcon(func)
            }

    //  view click listener as extension function
    private fun View.setClickListenerToDialogIcon(func: (() -> Unit)?) =
            setOnClickListener {
                func?.invoke()
            }
}




class LoadingDialog(context: Context) : BaseDialog() {
    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.loading_progress_layout, null)
    }
    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

}

fun Activity.createLoadingDialog(): AlertDialog =
        LoadingDialog(this).apply {
            isBackGroundTransparent = true
            cancelable = false
        }.create()


inline fun Activity.showLoginError(func: LoginErrorDialog.() -> Unit): Unit =
        LoginErrorDialog(this).apply {
            func()
        }.create().show()

