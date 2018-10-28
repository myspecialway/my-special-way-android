package org.myspecialway.ui.agenda

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * class to convert the time indexes to readable time units
 */
object AgendaIndex : TimeFactory {
    /**
     * Looks at the left number [->11 0] that represents the hours
     */
    override fun convertTimeFromIndex(timeIndex: String, hours: String) =
        Time(createHour(getHour(hours), getMinute(hours)), day(timeIndex), hours)

    private fun getHour(hours: String) = hours.substringBefore(":").toInt()

    private fun getMinute(display: String) = display.substringAfter(":").substringBefore("-").trim().toInt()

    fun getTimeIndex(timeIndex: String) = timeIndex.substringBefore('_')

    /**
     * Looks at the left number [1 0<-] that represents the days
     */
    private fun day(index: String) = when (index.takeLast(1)) {
        "0" -> "Sunday"
        "1" -> "Monday"
        "2" -> "Tuesday"
        "3" -> "Wednesday"
        "4" -> "Thursday"
        "5" -> "Friday"
        "6" -> "Saturday"
        else -> throw Exception("Index on day conversion doesn't exists.")
    }


    fun todayWeekIndex(calendar: Calendar): String {
        val day = calendar.get(Calendar.DAY_OF_WEEK)

        return when (day) {
            Calendar.SUNDAY    -> "Sunday"
            Calendar.MONDAY    -> "Monday"
            Calendar.TUESDAY   -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY  -> "Thursday"
            Calendar.FRIDAY    -> "Friday"
            Calendar.SATURDAY  -> "Saturday"

            else -> throw Exception("Day doesn't exists")
        }
    }
}

@Parcelize
data class Time(val date: Date, val dayDisplay: String, val timeDisplay: String) : Parcelable

interface TimeFactory {
    fun convertTimeFromIndex(timeIndex: String, hours: String): Time
}
/**
 * return the hour of the day based on @param hour
 */
fun createHour(hour: Int, minute: Int): Date {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, hour)
    cal.set(Calendar.MINUTE, minute)
    cal.set(Calendar.SECOND, 0)
    return cal.time
}
