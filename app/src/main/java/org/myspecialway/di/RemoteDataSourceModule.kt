package org.myspecialway.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import org.koin.dsl.module.module
import org.myspecialway.data.remote.RemoteDataSource
import org.myspecialway.data.remote.TokenInterceptor
import org.myspecialway.di.RemoteProperties.TEMP
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RemoteProperties {
//    const val BASE_URL = "http://msw-dev.eastus.cloudapp.azure.com:3000/"
    const val TEMP = "http://104.211.5.234:3000/"
}

val remoteDataSourceModel = module {

    single { createOkHttpClient(get()) }

    single { TokenInterceptor(get(),get() ) }

    single { createWebService<RemoteDataSource>(get(), TEMP) }

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
