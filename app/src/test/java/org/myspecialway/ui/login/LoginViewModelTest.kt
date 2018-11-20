package org.myspecialway.ui.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.test.runner.AndroidJUnitRunner
import android.view.View
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Flowable
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.TestSchedulerProvider
import org.myspecialway.session.Token
import org.myspecialway.ui.agenda.AgendaState
import org.myspecialway.utils.fakeToken


class LoginViewModelTest : AndroidJUnitRunner() {

    val mockAuth = LoginAuthData().apply {
        username = "student"
        password = "Aa123456"
    }

    private lateinit var viewModel: LoginViewModel

    @Mock
    lateinit var view: Observer<LoginStates>

    @Mock
    lateinit var repository: LoginRepository


    @Mock
    private lateinit var sp: SharedPreferences

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {


        MockitoAnnotations.initMocks(this)

        val provider: SchedulerProvider = TestSchedulerProvider()

        viewModel = LoginViewModel(repository, provider, sp)

        viewModel.loginState.observeForever(view)
    }

    @Test
    fun `check login authentication LiveData events on Success`() {

        given(repository.performLogin(mockAuth)).willReturn(Flowable.just(LoginResponse(fakeToken)))

        viewModel.login(mockAuth)

        val arg = argumentCaptor<LoginStates>()

        verify(view, Mockito.times(3)).onChanged(arg.capture())

        val values = arg.allValues

        Assert.assertThat(values[0], Matchers.instanceOf(LoginStates.Progress::class.java))
        Assert.assertEquals((values[0] as LoginStates.Progress).view, View.VISIBLE)

        Assert.assertThat(values[1], Matchers.instanceOf(LoginStates.Success::class.java))

        Assert.assertThat(values[2], Matchers.instanceOf(LoginStates.Progress::class.java))
        Assert.assertEquals((values[2] as LoginStates.Progress).view, View.GONE)
    }

//    @Test
//    fun checkIfLoggedIn() {
//        val token = Token(fakeToken, 123222,123222)
//        Token().apply{ storeAccessToken(sp, token) }
//        viewModel.checkIfLoggedIn()
//
//
//    }
}