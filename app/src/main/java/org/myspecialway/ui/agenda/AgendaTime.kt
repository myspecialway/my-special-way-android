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
    override fun convertTimeFromIndex(timeIndex: String, hours: String) = when (getTimeIndex(timeIndex)) {
        "0" -> Time(createHour(7), day(timeIndex), hours)
        "1" -> Time(createHour(8), day(timeIndex), hours)
        "2" -> Time(createHour(9), day(timeIndex), hours)
        "3" -> Time(createHour(10), day(timeIndex), hours)
        "4" -> Time(createHour(11), day(timeIndex), hours)
        "5" -> Time(createHour(12), day(timeIndex), hours)
        "6" -> Time(createHour(13), day(timeIndex), hours)
        "7" -> Time(createHour(14), day(timeIndex), hours)
        "8" -> Time(createHour(15), day(timeIndex), hours)
        "9" -> Time(createHour(16), day(timeIndex), hours)
        "10" -> Time(createHour(17), day(timeIndex),hours)
        "11" -> Time(createHour(18), day(timeIndex),hours)
        "12" -> Time(createHour(19), day(timeIndex),hours)
        "13" -> Time(createHour(20), day(timeIndex),hours)
        "14" -> Time(createHour(21), day(timeIndex),hours)

        else -> throw Exception("Index on time conversion doesn't exists.")
    }

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

    /**
     * return the hour of the day based on @param hour
     */
    private fun createHour(hour: Int): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return cal.time
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
