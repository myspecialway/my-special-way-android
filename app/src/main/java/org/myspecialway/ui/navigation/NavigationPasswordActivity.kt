package org.myspecialway.ui.navigation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import org.myspecialway.R
import org.myspecialway.common.Navigation
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_NEUTRAL
import android.support.v7.app.AlertDialog


class NavigationPasswordActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_password_layout)
    }

    fun onBackClick(view: View) {
        finish()
    }

    fun onSendButtonPressed(view: View) {
        var editText = findViewById<EditText>(R.id.nav_password_edit_text)
        if (editText.text.toString().equals("1234")) {
            Navigation.toNavigationDestinationsActivity(this)
        } else {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setMessage(".קוד שגוי. אנא נסה שנית")
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "אישור") { dialog, which -> dialog.dismiss()}
            alertDialog.show()
        }
    }
}