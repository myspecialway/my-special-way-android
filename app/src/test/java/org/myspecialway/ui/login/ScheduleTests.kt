package org.myspecialway.ui.login

import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.myspecialway.ui.agenda.AgendaRepository
import org.myspecialway.ui.agenda.AgendaViewModel

class ScheduleTests {

    @Mock
    lateinit var viewModel: AgendaViewModel

    @Mock
    lateinit var repository: AgendaRepository

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Check if schedule stream get subscribed`() {
        repository.getSchedule()
                .test()
                .assertSubscribed()
    }

    @Test
    fun `test get schedule failed`() {
        val error = Throwable("error")
        `when`(repository.getSchedule()).thenReturn(Flowable.error(error))
         Mockito.verify(viewModel).failure(error)
    }
}
