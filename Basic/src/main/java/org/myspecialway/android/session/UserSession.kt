package org.myspecialway.android.session

data class UserSession(val token: Token, val userData: UserData) {

    val isLoggedIn: Boolean
        get() = token.isValid
}
