package org.myspecialway.data.remote


import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.myspecialway.App
import org.myspecialway.common.logout
import org.myspecialway.session.SessionManager
import org.myspecialway.ui.login.LoginRepository

class TokenInterceptor(
        val repository: LoginRepository,
        val session: SessionManager) : Interceptor {

    private val headerKey = "Authorization"
    private fun headerValue(value: String) = "Bearer $value"

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val modifiedRequest: Request

        modifiedRequest = original.newBuilder()
                .addHeader(headerKey, headerValue(session.token ?: ""))
                .build()
        val response = chain.proceed(modifiedRequest)

        if (response.code() in 401..499) {

            repository.performLogin(session.getUserModel().authData!!).subscribe({
                // update the token on success
                session.updateToken(it.accessToken)
            }, {
                // logout on error
                App.instance?.applicationContext?.logout()
            })
        }
        return response
    }
}