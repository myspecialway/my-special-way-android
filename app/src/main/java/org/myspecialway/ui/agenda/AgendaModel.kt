package org.myspecialway.ui.agenda

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

@Entity()
data class ScheduleModel(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "sche_model_id") @NonNull val id: Int,
        @Embedded() val data: Data)

@Entity()
data class Data(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "data_id") @NonNull val id: Int,
        @SerializedName("student") @Embedded val classById: Class)


@Entity()
data class Class(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "class_id") @NonNull val id: Int,
        val name: String? = null,
        val number: Int? = null,
        val level: String? = null,
        val scheduleList: List<Schedule>? = null)

@Entity()
data class Schedule(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "schedule_id") @NonNull val id: Int,
        val index: String? = null,
        @Embedded val lesson: Lesson? = null)

@Entity()
data class Lesson(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "lesson_id") @NonNull val id: Int,
        val title: String? = null,
        val icon: String? = null)

// UI Models
data class ScheduleRenderModel(var title: String? = null,
                               var image: Int? = null,
                               var time: Time? = null,
                               var isNow: Boolean = false)