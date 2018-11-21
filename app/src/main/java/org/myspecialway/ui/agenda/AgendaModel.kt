package org.myspecialway.ui.agenda

import android.arch.persistence.room.*
import android.os.Parcelable
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.myspecialway.common.ViewType
import java.util.*

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
        val hours: String? = null,
        @Embedded val lesson: Lesson,
        @Embedded val location: Location? = null)

@Entity
data class Location(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "location_id") @NonNull val id: Int,
                    @SerializedName("location_id") val locationId: String? = null,
                    val name: String? = null,
                    val disabled: Boolean? = null,
                    var pinned: Boolean = false
)

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
                               var hours: String? = null,
                               var time: Time? = null,
                               var isNow: Boolean = false,
                               var isLast: Boolean = false,
                               var unityDest: String = "") : Parcelable, ViewType {

    override fun getViewType(): Int = AgendaTypes.ITEM_TYPE
}

data class SingleImageRes(val image: Int) : ViewType {
    override fun getViewType(): Int = AgendaTypes.SINGLE_IMAGE
}

@Entity()
data class LocationData(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "loc_data_id") @NonNull val id: Int,
        @SerializedName("locations") val locations: List<Location>)

@Entity
data class LocationModel(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "loc_model_id") @NonNull val id: Int,
        val data: LocationData)

object AgendaTypes {
    const val ITEM_TYPE = 0
    const val SINGLE_IMAGE = 1
}


fun mapScheduleRenderModel(schedule: Schedule) = ScheduleRenderModel()
        .apply {
            val display = schedule.hours ?: "7:30 - 08:00"
            val currentTime = Calendar.getInstance(TimeZone.getDefault()).time
            index = schedule.index
            title = schedule.lesson.title
            this.hours = schedule.hours
            unityDest = schedule.location?.locationId ?: "C1"
            image = schedule.lesson.icon
            time = schedule.index.let { AgendaIndex.convertTimeFromIndex(it, display) }
            isNow = currentTime.after(time?.date) && currentTime.before(createHour(hour(display), min(display)))
        }
private fun min(h: String): Int = h.substringAfter("-")
        .trim()
        .split(":")[1]
        .toInt()


private fun hour(h: String): Int = h.substringAfter("-")
        .trim()
        .split(":")[0]
        .toInt()