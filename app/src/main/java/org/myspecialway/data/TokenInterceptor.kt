package org.myspecialway.data

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.myspecialway.App
import org.myspecialway.session.UserSession
import org.myspecialway.ui.login.RequestCallback
import org.myspecialway.ui.login.gateway.InvalidLoginCredentials


class TokenInterceptor : Interceptor {

    private val headerKey = "Authorization"
    private fun headerValue(value: String) = "Bearer $value"

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val modifiedRequest: Request

        val sessionManager = App.instance?.userSessionManager

        modifiedRequest = original.newBuilder()
                .addHeader(headerKey, headerValue(sessionManager?.token ?: ""))
                .build()
        val response = chain.proceed(modifiedRequest)


        if (response.code() in 401..499) {
            // logout
            App.instance?.userSessionManager?.refreshSessionIfNeeded(object : RequestCallback<UserSession> {
                override fun onFailure(t: Throwable?) {
                    when(t) {
                        InvalidLoginCredentials() -> sessionManager?.logout(App.instance!!.applicationContext)
                        else -> Log.d("token", "retry 3 times")
                    }
                }

                override fun onSuccess(result: UserSession?) {
                    sessionManager?.storeToken(result?.token!!.accessToken)
                }
            })
        }

        return response
    }


}