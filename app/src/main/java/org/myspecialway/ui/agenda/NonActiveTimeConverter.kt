package org.myspecialway.ui.agenda

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.myspecialway.common.toJson

class NonActiveTimeConverter {
    @TypeConverter
    fun fromDataModel(data: List<NonActiveTimes>?): String? {
        return data?.toJson()
    }

    @TypeConverter
    fun fromString(value: String): List<NonActiveTimes> {
        val listType = object : TypeToken<List<NonActiveTimes>>() {

        }.type
        return Gson().fromJson<List<NonActiveTimes>>(value, listType)
    }

}