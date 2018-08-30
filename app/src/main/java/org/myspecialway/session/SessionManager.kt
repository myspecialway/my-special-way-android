package org.myspecialway.session

import android.content.SharedPreferences
import com.google.gson.Gson
import org.myspecialway.session.jwt.TokenPayloadData
import org.myspecialway.ui.agenda.USER_MODEL
import org.myspecialway.ui.login.UserModel
import org.myspecialway.utils.TokenParser


//class UserSessionManager(private val userModel: UserModel, token: Token) {



//    val token: String? get() = getUserModel().token?.accessToken
//
//    val isLoggedIn: Boolean = getUserModel().token != null


//    fun parseToken(token: String): TokenPayloadData = TokenParser().parsePayload(token)

//    fun storeUserModel(preferences: SharedPreferences, userModel: UserModel) =
//            preferences.edit().apply {
//                putString(USER_MODEL, Gson().toJson(userModel))
//            }.apply()
//
//    fun getUserModel(preferences: SharedPreferences): UserModel {
//        if (preferences.contains(USER_MODEL)) {
//            return Gson().fromJson<UserModel>(preferences.getString(USER_MODEL, ""), UserModel::class.java)
//        }
//
//        return UserModel()
//
//    }

//    // update the token in the user
//    fun updateToken(accessToken: String) {
//        val user = getUserModel()
//                .apply { this.token?.accessToken = accessToken }
//        storeUserModel(user)
//    }
//}