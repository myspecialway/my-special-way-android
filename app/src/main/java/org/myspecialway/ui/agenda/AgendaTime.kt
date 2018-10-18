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
    override fun convertTimeFromIndex(timeIndex: String) = when (getTimeIndex(timeIndex)) {
        "0" -> Time(createHour(7), day(timeIndex), "8:00 - 7:00")
        "1" -> Time(createHour(8), day(timeIndex), "9:00 - 8:00")
        "2" -> Time(createHour(9), day(timeIndex), "10:00 - 9:00")
        "3" -> Time(createHour(10), day(timeIndex), "11:00 - 10:00")
        "4" -> Time(createHour(11), day(timeIndex), "12:00 - 11:00")
        "5" -> Time(createHour(12), day(timeIndex), "13:00 - 12:00")
        "6" -> Time(createHour(13), day(timeIndex), "14:00 - 13:00")
        "7" -> Time(createHour(14), day(timeIndex), "15:00 - 14:00")
        "8" -> Time(createHour(15), day(timeIndex), "16:00 - 15:00")
        "9" -> Time(createHour(16), day(timeIndex), "17:00 - 16:00")
        "10" -> Time(createHour(17), day(timeIndex), "18:00 - 17:00")
        "11" -> Time(createHour(18), day(timeIndex), "19:00 - 18:00")
        "12" -> Time(createHour(19), day(timeIndex), "20:00 - 19:00")
        "13" -> Time(createHour(20), day(timeIndex), "21:00 - 20:00")
        "14" -> Time(createHour(21), day(timeIndex), "22:00 - 21:00")

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
    fun convertTimeFromIndex(timeIndex: String): Time
}
