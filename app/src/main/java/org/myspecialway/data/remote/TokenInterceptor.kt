package org.myspecialway.data.remote


import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.myspecialway.session.Token

class TokenInterceptor(val token: Token) : Interceptor {

    private val headerKey = "Authorization"
    private fun headerValue(value: String) = "Bearer $value"

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val modifiedRequest: Request

        modifiedRequest = original.newBuilder()
                .addHeader(headerKey, headerValue(token.accessToken ?: ""))
                .build()
        val response = chain.proceed(modifiedRequest)

//        if (response.code() in 401..499) {
//
//            repository.performLogin(session.getUserModel().authData!!).subscribe({
//                // update the token on allowNext
//                session.updateToken(it.accessToken)
//            }, {
//                // logout on error
//                App.instance?.applicationContext?.logout()
//            })
//        }
        return response
    }
}