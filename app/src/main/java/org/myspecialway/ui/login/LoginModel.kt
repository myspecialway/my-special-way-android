package org.myspecialway.ui.login

import org.myspecialway.session.SessionManager
import org.myspecialway.session.Token

data class LoginResponse(var accessToken: String)

/**
 * Entity contains the username and password entered in the LoginScreen
 */
data class AuthData(var username: String? = null, var password: String? = null)

/**
 * Entity contains the UserModel
 */
data class UserModel(
        var firstName: String? = null,
        var lastName: String? = null,
        var id: String? = null,
        var username: String? = null,
        var photo: String?= null,
        var role: String? = null,
        var authData: AuthData? = null,
        var token: Token? = null) {

    fun fullName(): String = "$firstName $lastName"

    fun UserModel.mapTokenUser(loginResponse: LoginResponse, authData: AuthData, sessionManager: SessionManager) {
        val parsed = sessionManager.parseToken(loginResponse.accessToken)
        this.id = parsed.id
        this.token = Token(loginResponse.accessToken, parsed.iat, parsed.exp)
        this.authData = authData
        this.firstName = parsed.firstname
        this.lastName = parsed.lastname
    }
}

