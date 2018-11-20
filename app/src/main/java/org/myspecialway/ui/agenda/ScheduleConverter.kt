package org.myspecialway.ui.agenda

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.myspecialway.common.toJson

class ScheduleConverter {
    @TypeConverter
    fun fromDataModel(data: List<Schedule>): String? {
        return data.toJson()
    }

    @TypeConverter
    fun fromString(value: String): List<Schedule> {
        val listType = object : TypeToken<List<Schedule>>() {

        }.type
        return Gson().fromJson<List<Schedule>>(value, listType)
    }
}
