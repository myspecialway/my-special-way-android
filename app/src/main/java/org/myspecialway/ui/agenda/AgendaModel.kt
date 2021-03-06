package org.myspecialway.ui.agenda

import android.arch.persistence.room.*
import android.os.Parcelable
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.myspecialway.common.ViewType
import java.text.SimpleDateFormat
import java.util.*

private val serverDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z")

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
        @SerializedName("reminders") val reminder: List<Reminder>?,
        val nonActiveTimes: List<NonActiveTimes>


)

@Entity()
data class Schedule(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "schedule_id") @NonNull val id: Int,
        val index: String,
        val hours: String? = null,
        @Embedded val lesson: Lesson,
        @Embedded val location: Location? = null)

@Entity
@Parcelize
data class Location(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "location_id") @NonNull val id: Int,
                    @SerializedName("location_id") val locationId: String? = null,
                    val name: String? = null,
                    val disabled: Boolean? = null,
                    var pinned: Boolean = false,
                    @Embedded var  position: Position? = null,
                    @SerializedName("icon") var locationIcon: String? = null,
                    var type: String? = null
) : Parcelable


@Entity
data class BlockedSection(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") @NonNull val id: Int,
                          val reason: String? = null,
                          val from: String? = null,
                          val to: String? = null
)

@Entity()
@Parcelize
data class Position(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "floor") @NonNull val floor: Int)  : Parcelable

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
        @SerializedName("daysindex") val daysindex: List<Int>,
        val hours: List<String>) : Parcelable


@Entity()
data class LocationData(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "loc_data_id") @NonNull val id: Int,
                        @SerializedName("locations") val locations: List<Location>)

@Entity()
data class BlockedSectionsData(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "block_sec_model_id") @NonNull val id: Int,
                        @SerializedName("blockedSections") val blockedSections: List<BlockedSection>)

@Entity
data class LocationModel(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "loc_model_id") @NonNull val id: Int,
        val data: LocationData)


@Entity()
data class NonActiveTimes(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "nonActiveTime_id") @NonNull val id: Int,
        val title: String = "",
        val startDateTime: String,
        val endDateTime: String,
        val isAllDayEvent: Boolean = false,
        val isAllClassesEvent: Boolean = false)

@Entity
data class BlockedSectionsModel(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "block_sec_model_id") @NonNull val id: Int,
        val data: BlockedSectionsData)

// UI Models
@Parcelize
data class ScheduleRenderModel(var index: String? = null,
                               var title: String? = null,
                               var image: String? = null,
                               var hours: String? = null,
                               var time: Time? = null,
                               var isNow: Boolean = false,
                               var isLast: Boolean = false,
                               var unityDest: Location? =null) : Parcelable, ViewType {

    override fun getViewType(): Int = AgendaTypes.ITEM_TYPE
}

@Parcelize
data class ReminderRenderModel(var enabled: Boolean = false,
                               var type: ReminderType = ReminderType.REHAB, // TODO: consider default
                               var reminderTime: List<ReminderTime> = listOf()) : Parcelable, ViewType {

    override fun getViewType(): Int = AgendaTypes.ITEM_TYPE
}

@Parcelize
data class NonActiveTimeRenderModel(
        var title: String = "",
        var startDateTime: Date? = null,
        var endDateTime: Date? = null,
        var isAllDayEvent: Boolean = false,
        var isAllClassesEvent: Boolean = false) : Parcelable, ViewType {

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
            return when (name) {
                ReminderType.MEDICINE.name -> ReminderType.MEDICINE
                ReminderType.REHAB.name -> ReminderType.REHAB
                ReminderType.SCHEDULE.name -> ReminderType.SCHEDULE
                else -> ReminderType.REHAB // TODO
            }
        }
    }
}

object AgendaTypes {
    const val ITEM_TYPE = 0
    const val SINGLE_IMAGE = 1
}

enum class DestinationType {
    MEDICINE, TOILET, REGULAR;
}

fun mapScheduleRenderModel(schedule: Schedule) = ScheduleRenderModel()
        .apply {
            val display = schedule.hours ?: "7:30 - 08:00"
            val currentTime = Calendar.getInstance(TimeZone.getDefault()).time
            index = schedule.index
            title = schedule.lesson.title
            this.hours = schedule.hours
            unityDest = schedule.location
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

fun mapNonActiveTimeRenderModel(nonActiveTime: NonActiveTimes) = NonActiveTimeRenderModel()
        .apply {
            title = nonActiveTime.title
            isAllDayEvent = nonActiveTime.isAllDayEvent
            startDateTime = parseDate(nonActiveTime.startDateTime)
            endDateTime = parseDate(nonActiveTime.endDateTime)
            isAllClassesEvent = nonActiveTime.isAllClassesEvent
        }

private fun parseDate(dateStr: String): Date? {
    try {
        return serverDateFormat.parse(dateStr)
    } catch (e: Throwable) {
        return null
    }
}

private fun min(h: String): Int = h.substringAfter("-")
        .trim()
        .split(":")[1]
        .toInt()


private fun hour(h: String): Int = h.substringAfter("-")
        .trim()
        .split(":")[0]
        .toInt()