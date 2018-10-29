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

class LocationConverter {
    @TypeConverter
    fun fromLocationModel(location: List<Location>): String? {
        return location.toJson()
    }

    @TypeConverter
    fun fromString(value: String): List<Location> {
        val listType = object: TypeToken<List<Location>>() {

        }.type
        return Gson().fromJson<List<Location>>(value, listType)
    }

}