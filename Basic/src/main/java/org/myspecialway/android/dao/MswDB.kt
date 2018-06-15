package org.myspecialway.android.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(Lesson::class)], version = 1)
abstract class MswDB : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}