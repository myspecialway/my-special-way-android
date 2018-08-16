package org.myspecialway.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import org.myspecialway.ui.agenda.ScheduleModel

@Database(entities = [ScheduleModel::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun localDataSourceDAO(): LocalDataSource
}