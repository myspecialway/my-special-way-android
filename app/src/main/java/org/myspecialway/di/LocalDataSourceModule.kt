package org.myspecialway.di

import android.arch.persistence.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext
import org.myspecialway.data.local.Database

val localDataSourceModule = applicationContext {
    bean {
        Room.databaseBuilder(androidApplication(), Database::class.java, "database")
                .fallbackToDestructiveMigration()
                .build()
    }

    bean { get<Database>().localDataSourceDAO() }
}