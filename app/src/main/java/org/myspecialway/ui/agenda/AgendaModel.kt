package org.myspecialway.ui.agenda

import android.arch.persistence.room.*
import android.os.Parcelable
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import io.reactivex.internal.util.BackpressureHelper
import kotlinx.android.parcel.Parcelize
import org.myspecialway.common.ViewType


val mockList = arrayListOf<Schedule>().apply {
    add(Schedule(12, "1_4", "07:15 - 8:15", Lesson(1, "בוקר טוב", ""), Location(1, "C1", "פטל", false, Position(1, 1.0, 1.2, 1))))
    add(Schedule(13, "2_4", "8:15 - 9:15", Lesson(2, "חשבון", ""), Location(2, "C1", "סחלב", false, Position(1, 1.0, 1.2, 1))))
    add(Schedule(14, "3_4", "9:15 - 10:00", Lesson(3, "הפסקה", ""), Location(3, "C1", "הפסקה", false, Position(1, 1.0, 1.2, 1))))
    add(Schedule(15, "4_4", "10:00 - 11:00", Lesson(4, "'קריאה", ""), Location(4, "C1", "תאנה", false, Position(1, 1.0, 1.2, 1))))
    add(Schedule(16, "5_4", "11:00 - 12:00", Lesson(5, "אומנות", ""), Location(5, "C1", "אלון", false, Position(1, 1.0, 1.2, 1))))
    add(Schedule(16, "6_4", "11:00 - 12:00", Lesson(6, "אוכל", ""), Location(6, "C1", "נרקיס", false, Position(1, 1.0, 1.2, 1))))
    add(Schedule(17, "7_4", "13:00 - 13:30", Lesson(1, "הפסקה", ""), Location(7, "C1", "הפסקה", false, Position(1, 1.0, 1.2, 1))))
    add(Schedule(18, "8_4", "13:30 - 14:30", Lesson(1, "ציור", ""), Location(8, "C1", "פטל", false, Position(1, 1.0, 1.2, 1))))
}

val mockRes = ScheduleModel(1, Data(1, Class(1, "כיתת ציור", 1, "level", mockList)))

@Entity()
data class ScheduleModel(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "sche_model_id") @NonNull val id: Int,
        @Embedded(prefix = "data_") val data: Data)

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
        val schedule: List<Schedule>

)

@Entity()
data class Schedule(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "schedule_id") @NonNull val id: Int,
        val index: String,
        val display: String,
        @Embedded val lesson: Lesson,
        @Embedded val location: Location? = null)

@Entity
data class Location(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "location_id") @NonNull val id: Int,
                    @SerializedName("location_id") val locationId: String? = null,
                    val name: String? = null,
                    val disabled: Boolean? = null,
                    @Embedded val position: Position? = null
)

@Entity
data class Position(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "position_id") @NonNull val id: Int,
                    val latitude: Double? = null,
                    val longitude: Double? = null,
                    val floor: Int? = null)

@Entity()
data class Lesson(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "lesson_id") @NonNull val id: Int,
        val title: String,
        val icon: String)

// UI Models
@Parcelize
data class ScheduleRenderModel(var index: String? = null,
                               var title: String? = null,
                               var image: String? = null,
                               var time: Time? = null,
                               var isNow: Boolean = false,
                               var unityDest: String = "") : Parcelable, ViewType {

    override fun getViewType(): Int = AgendaTypes.ITEM_TYPE
}

data class SingleImageRes(val image: Int) : ViewType {
    override fun getViewType(): Int = AgendaTypes.SINGLE_IMAGE
}


object AgendaTypes {
    const val ITEM_TYPE = 0
    const val SINGLE_IMAGE = 1
}
