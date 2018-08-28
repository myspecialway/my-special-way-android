package org.myspecialway.session

import android.content.SharedPreferences
import com.google.gson.Gson
import org.myspecialway.session.jwt.TokenPayloadData
import org.myspecialway.ui.login.UserModel
import org.myspecialway.utils.TokenParser

class SessionManager(private val preferences: SharedPreferences) {

    companion object {
        var USER_MODEL = "user_model"
    }

    val token: String? get() = getUserModel().token?.accessToken

    val isLoggedIn: Boolean = getUserModel().token != null 


    fun parseToken(token: String): TokenPayloadData = TokenParser().parsePayload(token)

    fun storeUserModel(userModel: UserModel) =
            preferences.edit().apply {
                putString(USER_MODEL, Gson().toJson(userModel))
            }.apply()

    fun getUserModel(): UserModel = Gson().fromJson<UserModel>(preferences.getString(USER_MODEL,""), UserModel::class.java)

    // update the token in the user
    fun updateToken(accessToken: String) {
        val user = getUserModel()
                .apply { this.token?.accessToken = accessToken }
        storeUserModel(user)
    }
}