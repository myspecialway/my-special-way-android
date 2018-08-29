package org.myspecialway.ui.login

import android.arch.lifecycle.MutableLiveData
import android.view.View
import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.Navigation
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.with
import org.myspecialway.session.SessionManager

class LoginViewModel(private val repository: LoginRepository,
                     private val schedulerProvider: SchedulerProvider,
                     private val sessionManager: SessionManager) : AbstractViewModel() {

    val loginLive = MutableLiveData<LoginData>()

    fun login(authData: AuthData) = launch {
        repository
                .performLogin(authData)
                .with(schedulerProvider)
                .doOnSubscribe { progress.value = View.VISIBLE }
                .doFinally { progress.value = View.GONE }
                .subscribe({
                    saveUserAfterLogin(it, authData)
                    loginLive.value = LoginSuccess(true)
                }, { loginLive.value = LoginError(it) }
                )
    }

    private fun saveUserAfterLogin(loginResponse: LoginResponse, authData: AuthData) =
        sessionManager.storeUserModel(UserModel().apply { mapTokenUser(loginResponse, authData, sessionManager) } )

    fun checkLoggedIn() {
        when(sessionManager.token?.isNotEmpty()) {
            true -> loginLive.value = LoginSuccess(true)
            false ->loginLive.value = LoginError(Throwable("Can't Login"))
        }
    }
}

// States
sealed class LoginData
data class LoginSuccess(val allowNext:Boolean) : LoginData()
data class LoginError(val throwable: Throwable) : LoginData()

