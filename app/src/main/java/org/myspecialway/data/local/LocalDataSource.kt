package org.myspecialway.data.local

import android.arch.persistence.room.*
import io.reactivex.Single
import org.myspecialway.ui.agenda.Location
import org.myspecialway.ui.agenda.LocationModel

import org.myspecialway.ui.agenda.ScheduleModel

@Dao
interface LocalDataSource {

    @Query("DELETE FROM schedulemodel")
    fun deleteAllSchedule()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllSchedule(schedule: ScheduleModel)

    @Query("SELECT * FROM schedulemodel")
    fun loadSchedule() : Single<ScheduleModel>

    @Query("DELETE FROM locationmodel")
    fun deleteAllLocations()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLocations(locations: LocationModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLocations(locations: List<Location>)

    @Query("SELECT * FROM locationmodel")
    fun loadLocations() : Single<LocationModel>
}