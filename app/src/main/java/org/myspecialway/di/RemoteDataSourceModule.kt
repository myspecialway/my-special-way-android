package org.myspecialway.di

import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import org.myspecialway.data.remote.RemoteDataSource
import org.myspecialway.data.remote.TokenInterceptor
import org.myspecialway.di.RemoteProperties.BASE_URL
import org.myspecialway.session.Token
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RemoteProperties {
    const val BASE_URL = "http://msw-dev.eastus.cloudapp.azure.com:3000/"
}

val remoteDataSourceModel = applicationContext {

    bean { createOkHttpClient(get()) }

    bean { TokenInterceptor(get(),get() ) }

    bean { createWebService<RemoteDataSource>(get(), BASE_URL) }
}

fun createOkHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(interceptor)
            .build()
}


inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String) : T {
    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    return retrofit.create(T::class.java)
}
