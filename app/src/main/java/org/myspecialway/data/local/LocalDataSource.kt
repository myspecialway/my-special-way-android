package org.myspecialway.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.myspecialway.ui.agenda.Location
import org.myspecialway.ui.agenda.LocationModel

import org.myspecialway.ui.agenda.ScheduleModel

@Dao
interface LocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllSchedule(schedule: ScheduleModel)

    @Query("SELECT * FROM schedulemodel")
    fun loadSchedule() : Single<ScheduleModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLocations(locations: LocationModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLocations(locations: List<Location>)

    @Query("SELECT * FROM locationmodel")
    fun loadLocations() : Single<LocationModel>
}