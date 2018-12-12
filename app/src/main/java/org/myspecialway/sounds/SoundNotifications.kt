package org.myspecialway.sounds

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import org.myspecialway.utils.Logger


class SoundNotifications {

    companion object {
        var mp : MediaPlayer? = null
        val playlist : MutableList<Pair<Context, Int>> = mutableListOf()

        fun playSoundNotification(context: Context, soundResourceId: Int) {
            if (mp?.isPlaying ?: false){
                playlist.add(Pair(context, soundResourceId))
                return;
            }
            mp = MediaPlayer.create(context, soundResourceId)
            try {
                mp?.start()
                mp?.setOnCompletionListener {
                    mp?.release()
                    mp = null
                    if (!playlist.isEmpty()){
                        val pair = playlist.removeAt(0)
                        playSoundNotification(pair.first, pair.second)
                    }
                }
            } catch (e: Exception) {
                Logger.e("SoundNotifications", "Error playing sound", e)
            }


        }
    }
}