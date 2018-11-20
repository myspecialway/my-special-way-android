package org.myspecialway.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import org.myspecialway.ui.agenda.*

@Database(entities = [
    ScheduleModel::class,
    Data::class,
    Class::class,
    Schedule::class,
    Lesson::class,
    Location::class,
    LocationData::class,
    LocationModel::class
], version = 1)
@TypeConverters(ScheduleConverter::class, LocationDataConverter::class, LocationConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun localDataSourceDAO(): LocalDataSource
}