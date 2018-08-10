package org.myspecialway.android.session

import org.myspecialway.android.login.RequestCallback
import org.myspecialway.android.login.gateway.ILoginGateway
import org.myspecialway.android.utils.JWTParser

class UserSessionManager(private val gateway: ILoginGateway, private val parser: JWTParser) {

    var userSession: UserSession? = null
        private set(value){
            field = value
        }

    val isLoggedIn: Boolean
        get() = userSession != null && userSession!!.isLoggedIn

    fun login(username: String, password: String, callback: RequestCallback<UserSession>) {

        if (username.isBlank() || password.isEmpty()) {
            throw IllegalArgumentException("username or password are invalid")
        }

        this.gateway.login(username, password, object : RequestCallback<String> {

            override fun onSuccess(result: String) {

                val tokenPayloadData = parser.parsePayload(result)

                val token = Token(result, tokenPayloadData.issuedTime, tokenPayloadData.expirationTime)

                userSession = UserSession(token, UserData(tokenPayloadData))

                callback.onSuccess(userSession)
            }

            override fun onFailure(t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    private fun storeCredentials(session: UserSession, password: String) {


    }
}
