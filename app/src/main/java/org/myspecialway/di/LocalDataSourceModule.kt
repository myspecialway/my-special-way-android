package org.myspecialway.di

import android.arch.persistence.room.Room
import android.preference.PreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import org.myspecialway.data.local.Database
import org.myspecialway.di.LocalProperties.DB_NAME

object LocalProperties {
    const val DB_NAME = "database"
}
val localDataSourceModule = module {

    single {
        Room.databaseBuilder(androidContext(), Database::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    single {  PreferenceManager.getDefaultSharedPreferences(androidContext()) }

    single { get<Database>().localDataSourceDAO() }
}