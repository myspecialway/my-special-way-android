package org.myspecialway.ui.main

import android.content.Context
import android.support.v7.app.AlertDialog
import org.myspecialway.common.Navigation


class NavDialog {
    var listItems = mutableListOf<MappedClassesModel>()

//            .apply {
//        add(MCM("כיתת סחלב", "B1"))
//        add(MCM("כיתת דקל", "B10"))
//        add(MCM("כיתת תמר", "B11"))
//        add(MCM("כיתת ניצן", "B12"))
//        add(MCM("כיתת נרקיס", "C3"))
//        add(MCM("כיתת מוסיקה", "C2"))
//        add(MCM("כיתת פטל", "C1"))
//    }

    fun showNavigationDialog(context: Context) {
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
}