package org.myspecialway.ui.login

import android.content.SharedPreferences
import com.google.gson.Gson
import org.myspecialway.common.toJson
import org.myspecialway.ui.agenda.USER_MODEL
import org.myspecialway.utils.TokenParser

/**
 * Login Response data
 */
data class LoginResponse(var accessToken: String)

/**
 * Entity contains the username and password entered in the LoginScreen
 */
data class LoginAuthData(var username: String? = null, var password: String? = null)

/**
 * Entity contains the UserModel
 */
data class UserModel(
        var firstName: String? = null,
        var lastName: String? = null,
        var id: String? = null,
        var username: String? = null,
        var photo: String? = null,
        var role: String? = null,
        var gender: String? = null,
        var authData: LoginAuthData? = null) {

    fun storeUserModel(sp: SharedPreferences, userModel: UserModel) =
            sp.edit().apply {
                putString(USER_MODEL, userModel.toJson())
            }.apply()

    fun getUser(preferences: SharedPreferences): UserModel {
        if (preferences.contains(USER_MODEL)) {
            val userModelString = preferences.getString(USER_MODEL, "")
            return Gson().fromJson(userModelString, UserModel::class.java)
        }

        return UserModel()

    }

    fun fullName(): String = "$firstName $lastName"


    fun map(loginResponse: LoginResponse, authData: LoginAuthData) = apply {
        val parsed = TokenParser().parsePayload(loginResponse.accessToken)
        this.id = parsed.id
        this.authData = authData
        this.firstName = parsed.firstname
        this.lastName = parsed.lastname
        this.gender = parsed.gender
    }
}

