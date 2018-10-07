package org.myspecialway.data.remote


import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.myspecialway.App
import org.myspecialway.common.logout
import org.myspecialway.di.RemoteProperties.BASE_URL
import org.myspecialway.session.Token
import org.myspecialway.ui.login.LoginAuthData
import org.myspecialway.ui.login.UserModel
import org.myspecialway.ui.login.buildJson
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TokenInterceptor(private val token: Token,
                       private val sp: SharedPreferences) : Interceptor {

    private val headerKey = "Authorization"
    private fun headerValue(value: String) = "Bearer $value"

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val modifiedRequest: Request

        modifiedRequest = original.newBuilder()
                .addHeader(headerKey, headerValue(token.getToken(sp).accessToken ?: ""))
                .build()
        val response = chain.proceed(modifiedRequest)

        if (response.code() in 401..499) {
            val auth = UserModel().getUser(sp).authData ?: LoginAuthData()
            refreshSilent(auth)
        }
        return response
    }

    private fun refreshSilent(auth: LoginAuthData) {
        val okHttp = OkHttpClient.Builder()
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .build()

        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttp)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RemoteDataSource::class.java)
                .performLogin(buildJson(auth)).subscribe({ res ->
                    token.let {
                        it.storeAccessToken(sp, it.map(res.accessToken))
                    }
                }, {
                    App.instance?.applicationContext?.logout()
                })
    }

}