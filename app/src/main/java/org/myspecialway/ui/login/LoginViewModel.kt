package org.myspecialway.ui.login

import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.view.View
import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.with
import org.myspecialway.session.Token

// States
sealed class LoginData

object LoginSuccess : LoginData()

class LoginViewModel(private val repository: LoginRepository,
                     private val schedulerProvider: SchedulerProvider,
                     private val sp: SharedPreferences) : AbstractViewModel() {

    val loginData = MutableLiveData<LoginData>()

    fun login(authData: LoginAuthData) = launch {
        repository
                .performLogin(authData)
                .with(schedulerProvider)
                .doOnSubscribe { progress.value = View.VISIBLE }
                .doFinally { progress.value = View.GONE }
                .subscribe({
                    loginData.value = LoginSuccess
                }, { failure(it) })
    }

    fun checkIfLoggedIn() {
        when (Token().getToken(sp).accessToken?.isNotEmpty()) {
            true -> loginData.value = LoginSuccess
            false -> failure(Throwable("Can't Login"))
        }
    }
}