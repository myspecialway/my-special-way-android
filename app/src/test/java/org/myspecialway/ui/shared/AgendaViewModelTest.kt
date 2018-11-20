package org.myspecialway.ui.shared

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.view.View
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Flowable
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.TestSchedulerProvider
import org.myspecialway.ui.agenda.AgendaState
import org.myspecialway.ui.mock.mockRes

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
        assertThat(values[0], instanceOf(AgendaState.Progress::class.java))
        assertEquals((values[0] as AgendaState.Progress).progress, View.VISIBLE)
        assertThat(values[1], instanceOf(AgendaState.ListState::class.java))
        assertThat(values[2], instanceOf(AgendaState.Progress::class.java))
        assertEquals((values[2] as AgendaState.Progress).progress, View.GONE)
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

}