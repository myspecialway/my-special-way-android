package org.myspecialway.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.login_dialog_error.view.*
import org.myspecialway.R
import org.myspecialway.common.BaseDialog

class LoginErrorDialog(context: Context) : BaseDialog() {
    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.login_dialog_error, null)
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
                dialog?.dismiss()
            }
}

inline fun Activity.showLoginError(func: LoginErrorDialog.() -> Unit): AlertDialog =
        LoginErrorDialog(this).apply {
            func()
        }.create()