package org.myspecialway.common

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import org.myspecialway.ui.agenda.AgendaActivity
import org.myspecialway.ui.agenda.ScheduleRenderModel

object Navigation {

    fun toScheduleActivity(context: Context) {
        context.startActivity(Intent(context, AgendaActivity::class.java))
    }

    fun toUnityNavigation(context: Context, schedule: ScheduleRenderModel) {
        try {
            val intent = Intent()
            intent.component = ComponentName("com.att.indar.poc", "com.unity3d.player.UnityPlayerActivity")
            intent.putExtra("destination", schedule.title)
            context.startActivity(intent)
        } catch (e: Exception) {

        }
    }
}