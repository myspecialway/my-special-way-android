package org.myspecialway.utils

import org.myspecialway.ui.agenda.Time
import java.util.*

fun getCurrentHourOfTheDay(timeModel: Time): Int {
    val cal = Calendar.getInstance(TimeZone.getDefault())
    cal.time = timeModel.date
    return cal.get(Calendar.HOUR_OF_DAY)
}

val timeIndexCombinations = arrayListOf<String>().apply {
    add("00")
    add("10")
    add("20")
    add("30")
    add("40")
    add("50")
    add("60")
    add("70")
    add("80")
    add("90")
    add("100")
    add("110")
    add("120")
    add("130")
    add("140")
}