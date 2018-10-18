package org.myspecialway.ui.main

import android.content.Context
import android.support.v7.app.AlertDialog
import org.myspecialway.common.Navigation

fun showNavigationDialog(context: Context) {
    val listItems = mutableListOf<MainScreenActivity.DialogModel>().apply {
        add(MainScreenActivity.DialogModel("כיתת פטל", "B1"))
        add(MainScreenActivity.DialogModel("כיתת סחלב", "B2"))
        add(MainScreenActivity.DialogModel("כיתת מוזיקה", "B10"))
        add(MainScreenActivity.DialogModel("כיתת ניצן", "B11"))
        add(MainScreenActivity.DialogModel("כיתת תמר", "B12"))
        add(MainScreenActivity.DialogModel("כיתת דקל", "C1"))
        add(MainScreenActivity.DialogModel("כיתת בעלי חיים", "C2"))
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