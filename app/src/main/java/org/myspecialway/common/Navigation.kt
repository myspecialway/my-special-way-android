package org.myspecialway.common

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import org.myspecialway.ui.agenda.AgendaActivity

object Navigation {

    fun toSchduleActivity(context: Context) {
        context.startActivity(Intent(context, AgendaActivity::class.java))
    }

    fun toUnityNavigation(context: Context, destination: String) {
        try {
            val intent = Intent()
            intent.component = ComponentName("com.att.indar.poc", "com.unity3d.player.UnityPlayerActivity")
            intent.putExtra("destination", destination)
            context.startActivity(intent)
        } catch (e: Exception) {

        }
    }
}