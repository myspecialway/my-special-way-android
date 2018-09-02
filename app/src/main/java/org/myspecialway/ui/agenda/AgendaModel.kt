package org.myspecialway.ui.agenda

import android.arch.persistence.room.*
import android.os.Parcelable
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.myspecialway.common.ViewType

@Entity()
data class ScheduleModel(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "sche_model_id")
        @NonNull val id: Int,
        @Embedded(prefix = "data_") val data: Data)

@Entity()
data class Data(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "data_id")
        @NonNull val id: Int,
        @SerializedName("student") @Embedded val classById: Class)

@Entity()
data class Class(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "class_id")
        @NonNull val id: Int,
        val name: String? = null,
        val number: Int? = null,
        val level: String? = null,
        val schedule: List<Schedule>)

@Entity()
data class Schedule(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "schedule_id")
        @NonNull val id: Int,
        val index: String,
        @Embedded val lesson: Lesson,
        @Embedded val location: Location)

@Entity
data class Location(@PrimaryKey(autoGenerate = true)
                    @ColumnInfo(name = "location_id")
                    @NonNull val id: Int,
                    val name: String? = null,
                    val disabled: Boolean? = null,
                    @Embedded val position: Position? = null)

@Entity
data class Position(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "position_id") @NonNull val id: Int,
                    val latitude: Double? = null,
                    val longitude: Double? = null,
                    val floor: Int? = null)

@Entity()
data class Lesson(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "lesson_id")
        @NonNull val id: Int,
        val title: String,
        val icon: String)

// UI Models
@Parcelize
data class ScheduleRenderModel(var title: String? = null,
                               var image: String? = null,
                               var time: Time? = null,
                               var isNow: Boolean = false) : Parcelable, ViewType {

    override fun getViewType(): Int = AgendaTypes.ITEM_TYPE
}

data class SingleImageRes(val image: Int) : ViewType {
    override fun getViewType(): Int = AgendaTypes.SINGLE_IMAGE
}


object AgendaTypes {
    const val ITEM_TYPE = 0
    const val SINGLE_IMAGE = 1
}
