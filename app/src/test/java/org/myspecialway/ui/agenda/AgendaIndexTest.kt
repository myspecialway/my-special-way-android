package org.myspecialway.ui.agenda

import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.myspecialway.utils.getCurrentHourOfTheDay
import org.myspecialway.utils.timeIndexCombinations



class AgendaIndexTest {


    @Test
    fun `convertTimeFromIndex with params 00 return Sunday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_0")
        assertEquals("Sunday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 01 return Monday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_1")
        assertEquals("Monday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 02 return Tuesday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_2")
        assertEquals("Tuesday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 03 return Wednesday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_3")
        assertEquals("Wednesday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 04 return Thursday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_4")
        assertEquals("Thursday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 05 return Friday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_5")
        assertEquals("Friday", todayAtSeven.dayDisplay)
    }

    @Test
    fun `convertTimeFromIndex with params 06 return Saturday`() {
        val todayAtSeven = AgendaIndex.convertTimeFromIndex("0_6")
        assertEquals("Saturday", todayAtSeven.dayDisplay)
    }


    @Test
    fun `convertTimeFromIndex return correct date with createHour`() {
        for (index in timeIndexCombinations) {

            val timeModel = AgendaIndex.convertTimeFromIndex(index)

            val hour = AgendaIndex.getTimeIndex(index).toInt() + 7

            assertEquals(getCurrentHourOfTheDay(timeModel), hour)
        }
    }

//    @Test
//    fun `index on time conversion doesn't exists should fail`() {
//        AssertionError(AgendaIndex.convertTimeFromIndex("33"))
//    }

    @Test
    fun `check convertTimeFromIndex throws an exception`() {
        val outOfScopeIndex = "150"
        Observable.fromCallable { AgendaIndex.convertTimeFromIndex(outOfScopeIndex) }.test()
                .assertError { true }
    }
}