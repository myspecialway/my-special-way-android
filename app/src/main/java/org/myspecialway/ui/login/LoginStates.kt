package org.myspecialway.ui.login

sealed class LoginStates {
    object Success : LoginStates()
    data class Failure(val throwable: Throwable) : LoginStates()
    data class Progress(val view: Int) : LoginStates()
}