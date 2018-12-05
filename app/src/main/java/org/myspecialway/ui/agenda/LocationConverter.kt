package org.myspecialway.ui.agenda

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.myspecialway.common.toJson

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

class LocationDataConverter {
    @TypeConverter
    fun fromLocationDataModel(locationData: LocationData): String? {
        return locationData.toJson()
    }

    @TypeConverter
    fun fromString(value: String): LocationData {
        val listType = object: TypeToken<LocationData>() {

        }.type
        return Gson().fromJson<LocationData>(value, listType)
    }
}