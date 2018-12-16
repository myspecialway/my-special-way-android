package org.myspecialway.ui.agenda

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.myspecialway.common.toJson

class ReminderConverter {
    @TypeConverter
    fun fromDataModel(data: List<Reminder>?): String? {
        return data?.toJson()
    }

    @TypeConverter
    fun fromString(value: String): List<Reminder> {
        val listType = object : TypeToken<List<Reminder>>() {

        }.type
        return Gson().fromJson<List<Reminder>>(value, listType)
    }
}