package org.myspecialway.ui.agenda

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class AgendaIndexTest {

    @Test
    fun `check day display - sunday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("00")
        assertEquals("Sunday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `check day display - monday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("01")
        assertEquals("Monday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `check day display - tuesday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("02")
        assertEquals("Tuesday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `check day display - wednesday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("03")
        assertEquals("Wednesday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `check day display - thursday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("04")
        assertEquals("Thursday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `check day display - friday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("05")
        assertEquals("Friday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `check day display - saturday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("06")
        assertEquals("Saturday", todayAtSeven.dayDisplay)
    }


    @Test
    fun `check todayWeekIndex`() {

        // get today's name in word
        val todayFormatter = AgendaIndex.todayWeekIndex()

        // get today represented by number from calendar
        val dayOfTheWeek = Calendar.getInstance()
                .get(Calendar.DAY_OF_WEEK)

        // format the day represent by number to day name
        val dayFormatter = AgendaIndex
                .formatDayNumber(dayOfTheWeek)

        assertEquals(dayFormatter, todayFormatter)
    }

    @Test
    fun `index on time conversion doesn't exists should fail`() {
        AssertionError(AgendaIndex.convertTimeFromIndex("33"))
    }
}