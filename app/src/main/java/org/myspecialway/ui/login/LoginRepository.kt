package org.myspecialway.ui.login

import android.content.SharedPreferences
import com.google.gson.JsonObject
import io.reactivex.Flowable
import org.myspecialway.data.remote.RemoteDataSource
import org.myspecialway.session.Token

interface LoginRepository {
    fun performLogin(auth: AuthData): Flowable<LoginResponse>
}

class LoginRepositoryImpl(private val remoteDataSource: RemoteDataSource,
                          private val sp: SharedPreferences) : LoginRepository {
    override fun performLogin(auth: AuthData): Flowable<LoginResponse> =
            remoteDataSource.performLogin(buildJson(auth)).toFlowable().doOnNext { res ->
                val mappedUser = UserModel().apply { mapTokenUser(res, authData!!) }
                val mappedToken = Token().apply { mapToken(res.accessToken) }
                saveLoginResponse(mappedUser, mappedToken)
            }

    private fun buildJson(authData: AuthData): JsonObject =
            JsonObject().apply {
                addProperty("username", authData.username)
                addProperty("password", authData.password)
            }

    private fun saveLoginResponse(userModel: UserModel, token: Token) {
        UserModel().storeUserModel(sp, userModel)
        Token().storeAccessToken(sp, token)
    }
}
