package org.myspecialway.ui.agenda

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*


class AgendaIndexTest {


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