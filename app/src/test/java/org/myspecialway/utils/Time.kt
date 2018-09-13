package org.myspecialway.utils

import org.myspecialway.ui.agenda.Time
import java.util.*

fun getCurrentHourOfTheDay(timeModel: Time): Int {
    val cal = Calendar.getInstance(TimeZone.getDefault())
    cal.time = timeModel.date
    return cal.get(Calendar.HOUR_OF_DAY)
}

val timeIndexCombinations = arrayListOf<String>().apply {
    add("0_0")
    add("1_0")
    add("2_0")
    add("3_0")
    add("4_0")
    add("5_0")
    add("6_0")
    add("7_0")
    add("8_0")
    add("9_0")
    add("10_0")
    add("11_0")
    add("12_0")
    add("13_0")
    add("14_0")
}