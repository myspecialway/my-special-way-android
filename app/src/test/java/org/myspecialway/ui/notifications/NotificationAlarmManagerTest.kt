package org.myspecialway.ui.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.hamcrest.collection.IsEmptyCollection
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class NotificationAlarmManagerTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val pendingIntent = mock(PendingIntent::class.java)

    // mock data
    private val alarmsQueue = mutableListOf<PendingIntent>().apply {
        add(pendingIntent)
        add(pendingIntent)
        add(pendingIntent)
        add(pendingIntent)
    }

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)

    }

    @Test
    fun cancelAllCheckClearQueueList() {

        // Arrange
        val alarmManager = mock(AlarmManager::class.java)

        // Act
        alarmsQueue.cancelAll(alarmManager)

        // Assert
        assertThat(alarmsQueue, IsEmptyCollection.empty())
    }


}