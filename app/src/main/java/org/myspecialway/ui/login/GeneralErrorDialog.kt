package org.myspecialway.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.login_dialog_error.view.*
import org.myspecialway.R
import org.myspecialway.common.BaseDialog

class GeneralErrorDialog(context: Context) : BaseDialog() {
    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.general_error_dialog, null)
    }

    val content: TextView by lazy {
        dialogView.findViewById<TextView>(R.id.content)
    }

    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    fun closeButtonClickListener(func: (() -> Unit)? = null) =
            with(dialogView.closeButton) {
                setClickListenerToDialogIcon(func)
            }

    private fun View.setClickListenerToDialogIcon(func: (() -> Unit)?) =
            setOnClickListener {
                func?.invoke()
            }
}

inline fun Activity.showGeneralErrorDialog(func: GeneralErrorDialog.() -> Unit): Unit =
        GeneralErrorDialog(this).apply {
            func()
        }.create().show()

inline fun Fragment.showGeneralErrorDialog(func: GeneralErrorDialog.() -> Unit): AlertDialog =
        GeneralErrorDialog(this.context!!).apply {
            func()
        }.create()
