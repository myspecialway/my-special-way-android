package org.myspecialway.ui.shared

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.TestSchedulerProvider
import org.myspecialway.ui.agenda.ScheduleModel

class AgendaViewModelTest {

    lateinit var viewModel: AgendaViewModel

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
    fun testGetSchedule() {
        val response = mock(ScheduleModel::class.java)

        given(repository.getSchedule()).willReturn(Flowable.just(response))

        viewModel.getDailySchedule()

        val arg = argumentCaptor<AgendaState>()

        verify(view, times(2)).onChanged(arg.capture())

        val values = arg.allValues


        print("asd")
    }
}