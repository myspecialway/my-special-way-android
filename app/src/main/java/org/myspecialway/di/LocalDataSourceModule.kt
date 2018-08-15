package org.myspecialway.di

import android.arch.persistence.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext
import org.myspecialway.dataSources.local.Database

val localDataSourceModule = applicationContext {
    bean {
        Room.databaseBuilder(androidApplication(), Database::class.java, "database")
                .build()
    }

    bean { get<Database>().localDataSourceDAO() }
}