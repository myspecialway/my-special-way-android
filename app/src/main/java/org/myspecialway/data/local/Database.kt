package org.myspecialway.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import org.myspecialway.ui.agenda.*

@Database(entities = [
    ScheduleModel::class,
    Data::class,
    Student::class,
    Schedule::class,
    Reminder::class,
    Lesson::class,
    Location::class,
    LocationData::class,
    LocationModel::class,
    NonActiveTimes::class
], version = 3)
@TypeConverters(ScheduleConverter::class,ReminderConverter::class,ReminderTimeConverter::class,
        LocationDataConverter::class, LocationConverter::class, NonActiveTimeConverter::class)

abstract class Database : RoomDatabase() {
    abstract fun localDataSourceDAO(): LocalDataSource
}