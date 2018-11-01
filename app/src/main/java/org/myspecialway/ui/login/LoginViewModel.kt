package org.myspecialway.ui.login

import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.view.View
import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.with
import org.myspecialway.session.Token

class LoginViewModel(private val repository: LoginRepository,
                     private val schedulerProvider: SchedulerProvider,
                     private val sp: SharedPreferences) : AbstractViewModel() {

    val loginState = MutableLiveData<LoginStates>()

    fun login(authData: LoginAuthData) = launch {
        repository
                .performLogin(authData)
                .with(schedulerProvider)
                .doOnSubscribe { loginState.value = LoginStates.Progress(View.VISIBLE) }
                .doFinally { loginState.value = LoginStates.Progress(View.GONE) }
                .subscribe({
                    loginState.value = LoginStates.Success
                }, { loginState.value = LoginStates.Failure(it) })
    }

    fun checkIfLoggedIn() {
        when (Token().getToken(sp).accessToken?.isNotEmpty()) {
            true -> loginState.value = LoginStates.Success
            false -> loginState.value = LoginStates.Failure(Throwable("Can't Login"))
        }
    }
}