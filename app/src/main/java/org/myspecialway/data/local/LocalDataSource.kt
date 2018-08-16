package org.myspecialway.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import org.myspecialway.ui.agenda.Schedule

@Dao
interface LocalDataSource {

    /**
     * Save Schedule
     */
    @Insert
    fun saveAllSchedule(schedule: List<Schedule>)
}