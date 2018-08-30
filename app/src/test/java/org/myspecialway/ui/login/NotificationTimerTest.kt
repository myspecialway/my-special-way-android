package org.myspecialway.ui.login

import org.junit.Test
import java.util.*

class NotificationTimerTest {
    @Test
    fun `check alarm timing`() {

        val today = Calendar.getInstance()

        today.timeInMillis = System.currentTimeMillis() + 5000
        print(today)
//    val cal = Calendar.getInstance()
//    cal.time =
//    cal.add(Calendar.HOUR_OF_DAY, hours)
//    return cal.time

    }
}