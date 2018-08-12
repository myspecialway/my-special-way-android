package org.myspecialway.android.session

import android.content.SharedPreferences
import org.myspecialway.android.login.RequestCallback
import org.myspecialway.android.login.gateway.ILoginGateway
import org.myspecialway.android.login.gateway.InvalidLoginCredentials
import org.myspecialway.android.utils.JWTParser

class UserSessionManager(private val gateway: ILoginGateway, private val parser: JWTParser, private val sharedPreferences: SharedPreferences) {

    private var userSession: UserSession? = null

    val token: String?
        get() = userSession?.token?.accessToken

    val userData: UserData?
        get() = userSession?.userData

    val isLoggedIn: Boolean
        get() = userSession != null && userSession!!.isLoggedIn

    val canRefreshSession: Boolean
        get() = storedUsername.isNotBlank() && storedPassword.isNotEmpty()

    private val storedUsername: String
        get() = sharedPreferences.getString("storedUsername", "")

    private val storedPassword: String
        get() = sharedPreferences.getString("storedPassword", "")

    init {
        val sharedPreferences = sharedPreferences
        if(sharedPreferences.contains("token")){
            loadDataFromToken(sharedPreferences.getString("token", ""))
        }
    }

    fun refreshSessionIfNeeded(callback: RequestCallback<UserSession>){
        if(isLoggedIn){
            callback.onSuccess(userSession)
        }
        else if(!canRefreshSession){
            callback.onFailure(InvalidLoginCredentials())
        }
        else {
            login(storedUsername, storedPassword, callback)
        }
    }

    fun login(username: String, password: String, callback: RequestCallback<UserSession>) {

        if (username.isBlank() || password.isEmpty()) {
            throw IllegalArgumentException("username or password are invalid")
        }

        this.gateway.login(username, password, object : RequestCallback<String> {

            override fun onSuccess(result: String) {

                loadDataFromToken(result)

                storeCredentials(result, username, password)

                callback.onSuccess(userSession)
            }

            override fun onFailure(t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    private fun loadDataFromToken(token: String) {
        val tokenPayloadData = parser.parsePayload(token)

        val tokenData = Token(token, tokenPayloadData.iat, tokenPayloadData.exp)

        userSession = UserSession(tokenData, UserData(tokenPayloadData))
    }

    private fun storeCredentials(token: String, username: String, password: String) {

        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.putString("storedUsername", username)
        editor.putString("storedPassword", password)
        editor.apply()
    }
}
