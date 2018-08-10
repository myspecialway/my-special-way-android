package org.myspecialway.android.utils

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.myspecialway.android.R

class LoadingProgressDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val contentLoadingProgressBar = activity?.layoutInflater?.inflate(R.layout.loading_progress_layout, null) as ContentLoadingProgressBar
        contentLoadingProgressBar.indeterminateTintList = ColorStateList.valueOf(Color.rgb(106, 0, 207))
        return AlertDialog.Builder(context!!).setView(contentLoadingProgressBar).create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}