package org.myspecialway.android.dao

import android.arch.persistence.room.*

@Entity
data class Lesson(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val title: String,
        val icon: Int

)

@Entity
data class Location(

        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val name: String,
        val disabled: Boolean
)

@Entity
data class TimeSlot(

        val index: String,
        val lesson: Lesson,
        val location: Location
)

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM Lesson")
    fun getAllLessons():List<Lesson>

    @Insert
    fun insert(lesson: Lesson)
}