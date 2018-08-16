package org.myspecialway.ui.agenda

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ScheduleModel(
        val data: Data
)

data class Class(
        val name: String,
        val number: Int,
        val level: String,
        val schedule: List<Schedule>)

data class Data(
        @SerializedName("student") val classById: Class)

data class Lesson(
        val title: String,
        val icon: String)

data class Schedule(
        val index: String,
        val lesson: Lesson,
        val location: Any)

data class ScheduleRenderModel(var title: String? = null,
                               var image: Int? = null,
                               var time: Time? = null,
                               var isNow: Boolean = false)