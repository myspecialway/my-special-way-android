package org.myspecialway.ui.shared

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.view.View
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Flowable
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.TestSchedulerProvider
import org.myspecialway.common.addHour
import org.myspecialway.common.getRemindersForToday
import org.myspecialway.ui.agenda.*
import org.myspecialway.ui.mock.mockRes
import org.myspecialway.utils.Logger
import java.text.SimpleDateFormat
import java.util.*

class AgendaViewModelTest {

    private lateinit var viewModel: AgendaViewModel

    @Mock
    lateinit var view: Observer<AgendaState>

    @Mock
    lateinit var repository: AgendaRepository


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)

        val provider: SchedulerProvider = TestSchedulerProvider()

        viewModel = AgendaViewModel(repository, provider)
        viewModel.states.observeForever(view)
    }

    @Test
    fun `check get daily schedule LiveData events on success`() {
        given(repository.getSchedule()).willReturn(Flowable.just(mockRes))

        viewModel.getDailySchedule()

        val arg = argumentCaptor<AgendaState>()

        verify(view, times(3)).onChanged(arg.capture())

        val values = arg.allValues

        // Expecting [Progress(VISIBLE), CurrentSchedule, Alarms, ListOfDailySchedule, Progress(GONE) ]
//        assertThat(values[0], instanceOf(AgendaState.Progress::class.java))
//        assertEquals((values[0] as AgendaState.Progress).progress, View.VISIBLE)
//        assertThat(values[1], instanceOf(AgendaState.ListState::class.java))
//        assertThat(values[2], instanceOf(AgendaState.Progress::class.java))
//        assertEquals((values[2] as AgendaState.Progress).progress, View.GONE)
    }

    @Test
    fun `check get daily schedule LiveData events on failure`() {

        val error = Throwable("error")
        given(repository.getSchedule()).willReturn(Flowable.error(error))

        viewModel.getDailySchedule()

        val arg = argumentCaptor<AgendaState>()

        verify(view, times(3)).onChanged(arg.capture())

        val values = arg.allValues

        // Expecting [Progress(VISIBLE), Failure, Progress(GONE) ]
        assertThat(values[0], instanceOf(AgendaState.Progress::class.java))
        assertEquals((values[0] as AgendaState.Progress).progress, View.VISIBLE)

        assertThat(values[1], instanceOf(AgendaState.Failure::class.java))

        assertThat(values[2], instanceOf(AgendaState.Progress::class.java))
        assertEquals((values[2] as AgendaState.Progress).progress, View.GONE)
    }

    // add test for alarms
    @Test
    fun `check when no alarms alarms event is not getting triggered`() {

    }

    @Test
    fun `check when reminders are in the past they are ignored`() {
        val dateFormat = SimpleDateFormat("HH:mm")
        val reminderTimeInThePast = dateFormat.format(Date(System.currentTimeMillis() - 3600000))
        val reminders = listOf(Reminder(1, true, ReminderType.MEDICINE.name, listOf(ReminderTime(5, listOf(6,5), listOf(reminderTimeInThePast)))))
        val reminderRenderModels = reminders.map { mapReminderRenderModel(it) }.toMutableList()
        val remindersForToday = reminderRenderModels.getRemindersForToday(listOf())
        assertTrue(remindersForToday.isEmpty())
    }

    @Test
    fun `check that only reminders for today are returned`() {
        val dateFormat = SimpleDateFormat("HH:mm")
        val date = Date()
        val dayIndexOfToday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) -1
        val dayIndexOfYesterday = (dayIndexOfToday + 4 )% 5
        val dayIndexOfTomorrow = (dayIndexOfToday + 1 )% 5

        val reminderTimeCurrent = dateFormat.format(date.addHour(1))
        val reminders = listOf(Reminder(1, true, ReminderType.MEDICINE.name, listOf(ReminderTime(5, listOf(dayIndexOfYesterday, dayIndexOfToday, dayIndexOfTomorrow), listOf(reminderTimeCurrent)))))
        val reminderRenderModels = reminders.map { mapReminderRenderModel(it) }.toMutableList()
        val remindersForToday = reminderRenderModels.getRemindersForToday(listOf())
        assertTrue(remindersForToday.size == 1)
    }

    @Test
    fun `check that duplicate reminders are ignored`() {
        val dateFormat = SimpleDateFormat("HH:mm")
        val dayIndexOfToday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) -1
        val reminderTime = dateFormat.format(Date(System.currentTimeMillis() + 3600000))
        val reminders = listOf(Reminder(1, true, ReminderType.MEDICINE.name, listOf(ReminderTime(5, listOf(6), listOf(reminderTime, reminderTime)))), Reminder(1, true, ReminderType.MEDICINE.name, listOf(ReminderTime(5, listOf(dayIndexOfToday), listOf(reminderTime, reminderTime)))))
        val reminderRenderModels = reminders.map { mapReminderRenderModel(it) }.toMutableList()
        val remindersForToday = reminderRenderModels.getRemindersForToday(listOf())
        assertTrue(remindersForToday.size == 1)
    }

    @Test
    fun `check when non active times are in the past they are ignored`() {
        val nonActiveTimes = NonActiveTimeRenderModel("holiday", Date().addHour(-2), Date().addHour(3),false, false)
        val dateFormat = SimpleDateFormat("HH:mm")
        val date = Date()
        val dayIndexOfToday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) -1

        val reminderTimeCurrent = dateFormat.format(date.addHour(1))
        val reminders = listOf(Reminder(1, true, ReminderType.MEDICINE.name, listOf(ReminderTime(5, listOf( dayIndexOfToday), listOf(reminderTimeCurrent)))))
        val reminderRenderModels = reminders.map { mapReminderRenderModel(it) }.toMutableList()
        val remindersForToday = reminderRenderModels.getRemindersForToday(listOf(nonActiveTimes))
        assertTrue(remindersForToday.size == 0)


    }

}