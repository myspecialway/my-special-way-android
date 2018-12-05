package org.myspecialway.ui.navigation

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_navigation_password_layout.*
import org.myspecialway.R
import org.myspecialway.common.Navigation


class NavigationPasswordActivity : AppCompatActivity() {
    var unityDest: String = ""

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_password_layout)
        unityDest = intent.getStringExtra(UNITY_DEST_KEY) ?: ""
    }

    fun onBackClick(view: View) {
        finish()
    }

    fun onSendButtonClick(view: View) {
        if (nav_password_edit_text.text.toString().equals("1234")) {
            if (unityDest.isEmpty()) {
                Navigation.toNavigationLocationsActivity(this, intent.getStringExtra(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY))

            	finish()
            } else {
                Navigation.toUnityNavigation(this, unityDest )
            }
        } else {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setMessage(".קוד שגוי. אנא נסה שנית")
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "אישור") { dialog, which -> dialog.dismiss()}
            alertDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        nav_password_edit_text.requestFocus()
        nav_password_edit_text.postDelayed({
            val imm =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
            imm.showSoftInput(nav_password_edit_text, InputMethodManager.SHOW_IMPLICIT);
        },100L)
    }

    companion object {
        const val UNITY_DEST_KEY = "unity_dest_key"
    }
}