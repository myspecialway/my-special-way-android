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
        "0" -> TimeRenderModel(createHour(7), "7:00 - 8:00")
        "1" -> TimeRenderModel(createHour(8), "8:00 - 9:00")
        "2" -> TimeRenderModel(createHour(9), "9:00 - 10:00")
        "3" -> TimeRenderModel(createHour(10), "10:00 - 11:00")
        "4" -> TimeRenderModel(createHour(11), "11:00 - 12:00")
        "5" -> TimeRenderModel(createHour(12), "12:00 - 13:00")
        "6" -> TimeRenderModel(createHour(13), "13:00 - 14:00")
        "7" -> TimeRenderModel(createHour(14), "14:00 - 15:00")
        "8" -> TimeRenderModel(createHour(15), "15:00 - 16:00")
        "9" -> TimeRenderModel(createHour(16), "16:00 - 17:00")
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

interface TimeFactory {
    fun convertTimeFromIndex(timeIndex: String): TimeRenderModel
}

data class TimeRenderModel(val date: Date, val display: String)