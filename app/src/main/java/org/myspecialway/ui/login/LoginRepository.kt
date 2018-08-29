package org.myspecialway.ui.login

import com.google.gson.JsonObject
import io.reactivex.Flowable
import org.myspecialway.data.remote.RemoteDataSource

interface LoginRepository {
    fun performLogin(auth: AuthData): Flowable<LoginResponse>
}

class LoginRepositoryImpl(private val remoteDataSource: RemoteDataSource) : LoginRepository {
    override fun performLogin(auth: AuthData): Flowable<LoginResponse> =
        remoteDataSource.performLogin(buildJson(auth)).toFlowable()

    private fun buildJson(authData: AuthData): JsonObject =
            JsonObject().apply {
                addProperty("username", authData.username)
                addProperty("password", authData.password)
            }
}