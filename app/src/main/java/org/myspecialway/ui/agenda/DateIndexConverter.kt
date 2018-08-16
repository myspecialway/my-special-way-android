package org.myspecialway.ui.agenda

import java.util.*

/**
 * class to convert the time indexes to readable time units
 */
object DateIndex : TimeFactory {
    /**
     * Looks at the left number [10<-] that represents the hours
     */
    override fun convertTimeFromIndex(timeIndex: String) = when (timeIndex.takeLast(1)) {
        "0" -> Time(createHour(7), getDayNameByIndex(timeIndex), "7:00 - 8:00")
        "1" -> Time(createHour(8), getDayNameByIndex(timeIndex),"8:00 - 9:00")
        "2" -> Time(createHour(9), getDayNameByIndex(timeIndex),"9:00 - 10:00")
        "3" -> Time(createHour(10),getDayNameByIndex(timeIndex), "10:00 - 11:00")
        "4" -> Time(createHour(11),getDayNameByIndex(timeIndex), "11:00 - 12:00")
        "5" -> Time(createHour(12),getDayNameByIndex(timeIndex), "12:00 - 13:00")
        "6" -> Time(createHour(13),getDayNameByIndex(timeIndex), "13:00 - 14:00")
        "7" -> Time(createHour(14),getDayNameByIndex(timeIndex), "14:00 - 15:00")
        "8" -> Time(createHour(15),getDayNameByIndex(timeIndex), "15:00 - 16:00")
        "9" -> Time(createHour(16),getDayNameByIndex(timeIndex), "16:00 - 17:00")
        else -> throw Exception("Index doesn't exists.")
    }

    /**
     * Looks at the left number [->10] that represents the days
     */
    private fun getDayNameByIndex(index: String) = when (index.take(1)) {
        "0" -> "Sunday"
        "1" -> "Monday"
        "2" -> "Tuesday"
        "3" -> "Wednesday"
        "4" -> "Thursday"
        "5" -> "Friday"
        "6" -> "Saturday"
        else -> throw Exception("Index doesn't exists.")
    }

    /**
     * return the hour of the day based on @param hour
     */
    private fun createHour(hour: Int): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR, hour)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return cal.time
    }
}

data class Time(val date: Date, val dayDisplay: String, val timeDisplay: String)

interface TimeFactory {
    fun convertTimeFromIndex(timeIndex: String): Time
}

