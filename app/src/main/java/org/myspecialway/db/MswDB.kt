package org.myspecialway.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import org.myspecialway.db.dao.Lesson
import org.myspecialway.db.dao.ScheduleDao

@Database(entities = [(Lesson::class)], version = 1)
abstract class MswDB : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}