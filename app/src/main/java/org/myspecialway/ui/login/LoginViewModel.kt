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
data class LoginError(val throwable: Throwable) : LoginData()

class LoginViewModel(private val repository: LoginRepository,
                     private val schedulerProvider: SchedulerProvider,
                     private val sp: SharedPreferences) : AbstractViewModel() {

    val loginLive = MutableLiveData<LoginData>()

    fun login(authData: LoginAuthData) = launch {
        repository
                .performLogin(authData)
                .with(schedulerProvider)
                .doOnSubscribe { progress.value = View.VISIBLE }
                .doFinally { progress.value = View.GONE }
                .subscribe({
                    loginLive.value = LoginSuccess
                }, { loginLive.value = LoginError(it) }
                )
    }

    fun checkLoggedIn() {
        when (Token().getToken(sp).accessToken?.isNotEmpty()) {
            true -> loginLive.value = LoginSuccess
            false -> loginLive.value = LoginError(Throwable("Can't Login"))
        }
    }
}