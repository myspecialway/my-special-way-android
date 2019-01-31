package org.myspecialway.ui.shared

import com.squareup.picasso.Picasso
import org.myspecialway.App
import org.myspecialway.di.RemoteProperties
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.utils.Logger

class ImagesUtils {
    companion object {
        public fun prefetchImages(list: List<ScheduleRenderModel>?) {
            Logger.d("prefetchImages", "prefetchImages()")
            val set = mutableSetOf<String>()
            list?.forEach {
                set.add("${RemoteProperties.BASE_URL_IMAGES}${it.image ?: ""}.png")
            }
            set.remove("${RemoteProperties.BASE_URL_IMAGES}.png") // if  images were null, we added one with empty name. remove it so it won't be fetched.
            val applicationContext = App.instance?.applicationContext

            set.forEach {
                Logger.d("prefetchImages", "prefetching image $it")
                Picasso.with(applicationContext)
                        .load(it)
                        .fetch()
            }
        }


    }
}