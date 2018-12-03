package org.myspecialway.ui.agenda

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.myspecialway.common.toJson

class ReminderTimeConverter {
    @TypeConverter
    fun fromDataModel(data: List<ReminderTime>): String? {
        return data.toJson()
    }

    @TypeConverter
    fun fromString(value: String): List<ReminderTime> {
        val listType = object : TypeToken<List<ReminderTime>>() {

        }.type
        return Gson().fromJson<List<ReminderTime>>(value, listType)
    }
}