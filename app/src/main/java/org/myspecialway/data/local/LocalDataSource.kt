package org.myspecialway.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import org.myspecialway.ui.agenda.ScheduleModel

@Dao
interface LocalDataSource {

    /**
     * Save Schedule
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllSchedule(schedule: ScheduleModel)

    @Query("SELECT * FROM schedulemodel")
    fun loadSchedule() : ScheduleModel
}