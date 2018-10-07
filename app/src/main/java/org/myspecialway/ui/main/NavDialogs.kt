package org.myspecialway.ui.main

import android.content.Context
import android.support.v7.app.AlertDialog
import org.myspecialway.common.Navigation


data class DialogModel(val name: String, val id: String)

fun showNavigationDialog(context: Context) {
    val listItems = mutableListOf<DialogModel>().apply {
        add(DialogModel("כיתת סחלב", "B1"))
        add(DialogModel("כיתת דקל", "B10"))
        add(DialogModel("כיתת תמר", "B11"))
        add(DialogModel("כיתת ניצן", "B12"))
        add(DialogModel("כיתת נרקיס", "C3"))
        add(DialogModel("כיתת מוסיקה", "C2"))
        add(DialogModel("כיתת פטל", "C1"))
    }

    val builder = AlertDialog.Builder(context)
    builder.setTitle("נווט")
    builder.setSingleChoiceItems(listItems.map { it.name }.toTypedArray(), -1) { dialogInterface, i ->
        Navigation.toUnityNavigation(context, listItems[i].id)
        dialogInterface.dismiss()
    }
    // Set the neutral/cancel button click listener
    builder.setNeutralButton("בטל") { dialog, _ ->
        // Do something when click the neutral button
        dialog.cancel()
    }

    val dialog = builder.create()
    dialog.show()
}