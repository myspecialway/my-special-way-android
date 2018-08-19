package org.myspecialway.ui.agenda

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import com.google.gson.annotations.SerializedName
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


//class Converters {
//
//    @TypeConverter
//    fun fromString(value: String): ArrayList<String> {
//        val listType = object : TypeToken<ArrayList<String>>() {
//
//        }.type
//        return Gson().fromJson<Any>(value, listType)
//    }
//
//    @TypeConverter
//    fun fromArrayLisr(list: ArrayList<String>): String {
//        val gson = Gson()
//        return gson.toJson(list)
//    }
//}

@Entity()
data class ScheduleModel(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val data: Data
)

@Entity()
data class Data(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @SerializedName("student") val classById: Class)


@Entity()
data class Class(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val name: String,
        val number: Int,
        val level: String,
        val schedule: List<Schedule>)



@Entity()
data class Schedule(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val index: String,
        val lesson: Lesson,
        val location: Any)


@Entity
data class Lesson(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val title: String,
        val icon: String)



// UI Models
data class ScheduleRenderModel(var title: String? = null,
                               var image: Int? = null,
                               var time: Time? = null,
                               var isNow: Boolean = false)

