package org.myspecialway.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import org.myspecialway.data.remote.RemoteDataSource
import org.myspecialway.data.remote.TokenInterceptor
import org.myspecialway.di.RemoteProperties.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RemoteProperties {
    const val BASE_URL = "http://mswppr.tel-aviv.gov.il"
    const val BASE_URL_IMAGES = "http://mswppr.tel-aviv.gov.il/assets/MSW%20Symbols/"
}

val remoteDataSourceModel = module {

    single { createOkHttpClient(get()) }

    single { TokenInterceptor(get(), get()) }

    single { createWebService<RemoteDataSource>(get(), BASE_URL) }

}

fun createOkHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(tokenInterceptor)
        .addInterceptor(addOkHttpLog())
        .build()


fun addOkHttpLog(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}


inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    return retrofit.create(T::class.java)
}
