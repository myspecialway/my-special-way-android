package org.myspecialway.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.confirmation_dialog.view.*
import org.myspecialway.R
import org.myspecialway.common.BaseDialog

class ConfirmationDialog(context: Context) : BaseDialog() {
    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.confirmation_dialog, null)
    }

    val content: TextView by lazy {
        dialogView.findViewById<TextView>(R.id.content)
    }

    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    fun cancelButtonClickListener(func: (() -> Unit)? = null) =
            with(dialogView.cancelButton) {
                setClickListenerToDialogIcon(func)
            }

    fun approveButtonClickListener(func: (() -> Unit)? = null) =
            with(dialogView.approveButton) {
                setClickListenerToDialogIcon(func)
            }

    //  view click listener as extension function
    private fun View.setClickListenerToDialogIcon(func: (() -> Unit)?) =
            setOnClickListener {
                func?.invoke()
                dialog?.dismiss()
            }
}



inline fun Activity.showLogoutDialog(func: ConfirmationDialog.() -> Unit): Unit =
        ConfirmationDialog(this).apply {
            func()
        }.create().show()

inline fun Fragment.showLogoutDialog(func: ConfirmationDialog.() -> Unit): AlertDialog =
        ConfirmationDialog(this.context!!).apply {
            func()
        }.create()