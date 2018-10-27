package org.myspecialway.ui.agenda

import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.myspecialway.utils.getCurrentHourOfTheDay
import org.myspecialway.utils.timeIndexCombinations
import java.util.*


class AgendaIndexTest {


    @Test
    fun `convertTimeFromIndex with params 00 return Sunday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_0", schedule.hours)
        assertEquals("Sunday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 01 return Monday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_1", schedule.hours)
        assertEquals("Monday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 02 return Tuesday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_2", schedule.hours)
        assertEquals("Tuesday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 03 return Wednesday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_3", schedule.hours)
        assertEquals("Wednesday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 04 return Thursday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_4", schedule.hours)
        assertEquals("Thursday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 05 return Friday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_5", schedule.hours)
        assertEquals("Friday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 06 return Saturday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_6", schedule.hours)
        assertEquals("Saturday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 07 return Exception`() {
        Observable.fromCallable { AgendaIndex.convertTimeFromIndex("0_7", schedule.hours) }.test()
                .assertError { true }
    }


    @Test
    fun `convertTimeFromIndex return correct date with createHour`() {
        for (index in timeIndexCombinations) {

            val timeModel = AgendaIndex.convertTimeFromIndex(index, schedule.hours)

            val hour = AgendaIndex.getTimeIndex(index).toInt() + 7

            assertEquals(getCurrentHourOfTheDay(timeModel), hour)
        }
    }


    @Test
    fun `check convertTimeFromIndex throws an exception`() {
        val outOfScopeIndex = "150"
        Observable.fromCallable { AgendaIndex.convertTimeFromIndex(outOfScopeIndex, schedule.hours) }.test()
                .assertError { true }
    }

    @Test
    fun `todayWeekIndex return Sunday`() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val todayWeekIndex = AgendaIndex.todayWeekIndex(calendar)
        assertEquals(todayWeekIndex, "Sunday")
    }

    @Test
    fun `todayWeekIndex return Monday`() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val todayWeekIndex = AgendaIndex.todayWeekIndex(calendar)
        assertEquals(todayWeekIndex, "Monday")
    }

    @Test
    fun `todayWeekIndex return Tuesday`() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
        val todayWeekIndex = AgendaIndex.todayWeekIndex(calendar)
        assertEquals(todayWeekIndex, "Tuesday")
    }

    @Test
    fun `todayWeekIndex return Wednesday`() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
        val todayWeekIndex = AgendaIndex.todayWeekIndex(calendar)
        assertEquals(todayWeekIndex, "Wednesday")
    }

    @Test
    fun `todayWeekIndex return Thursday`() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
        val todayWeekIndex = AgendaIndex.todayWeekIndex(calendar)
        assertEquals(todayWeekIndex, "Thursday")
    }

    @Test
    fun `todayWeekIndex return Friday`() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
        val todayWeekIndex = AgendaIndex.todayWeekIndex(calendar)
        assertEquals(todayWeekIndex, "Friday")
    }

    @Test
    fun `todayWeekIndex return Saturday`() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        val todayWeekIndex = AgendaIndex.todayWeekIndex(calendar)
        assertEquals(todayWeekIndex, "Saturday")
    }

}