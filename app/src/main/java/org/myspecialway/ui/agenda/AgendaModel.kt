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
        @SerializedName("student") @Embedded val student: Student)


@Entity()
data class Student(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "class_id") @NonNull val id: Int,
        val name: String? = null,
        val number: Int? = null,
        val level: String? = null,
        val schedule: List<Schedule>,
        @SerializedName("reminders") val reminder : List<Reminder>?

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

@Entity()
data class Reminder(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "reminder_id") @NonNull val id: Int,
        val enabled: Boolean = false,
        val type: String,
        val schedule: List<ReminderTime>)

@Entity()
@Parcelize
data class ReminderTime(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "reminder_time_id") @NonNull val id: Int,
        val daysIndex: List<Int>,
        val hours: List<String>) : Parcelable

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

@Parcelize
data class ReminderRenderModel(var enabled: Boolean = false,
                               var type: ReminderType = ReminderType.REHAB, // TODO: consider default
                               var reminderTime : List<ReminderTime> = listOf()) : Parcelable, ViewType {

    override fun getViewType(): Int = AgendaTypes.ITEM_TYPE
}

data class SingleImageRes(val image: Int) : ViewType {
    override fun getViewType(): Int = AgendaTypes.SINGLE_IMAGE
}

enum class ReminderType(name: String) {
    MEDICINE("MEDICINE"),
    REHAB("REHAB"),
    SCHEDULE("SCHEDULE"); // added to distinguish between alerts

    companion object {
        fun byName(name: String): ReminderType {
//            return if (ReminderType.MEDICINE.name == name) ReminderType.MEDICINE else ReminderType.REHAB
            return when (name) {
                ReminderType.MEDICINE.name  -> ReminderType.MEDICINE
                ReminderType.REHAB.name     -> ReminderType.REHAB
                ReminderType.SCHEDULE.name     -> ReminderType.SCHEDULE
                else -> ReminderType.REHAB // TODO
            }





//            if (ReminderType.MEDICINE.name == name) ReminderType.MEDICINE else ReminderType.REHAB
        }
    }
}

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

fun mapReminderRenderModel(reminder: Reminder) = ReminderRenderModel()
        .apply {
            enabled = reminder.enabled
            type = ReminderType.byName(reminder.type)
            reminderTime = reminder.schedule
        }

private fun min(h: String): Int = h.substringAfter("-")
        .trim()
        .split(":")[1]
        .toInt()


private fun hour(h: String): Int = h.substringAfter("-")
        .trim()
        .split(":")[0]
        .toInt()