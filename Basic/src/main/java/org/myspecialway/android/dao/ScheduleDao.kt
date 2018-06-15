package org.myspecialway.android.dao

import android.arch.persistence.room.*
import java.time.DayOfWeek

@Entity
data class Lesson(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val day: DayOfWeek,
        val startTimeHour: Int,
        val startTimeMinutes: Int,
        val endTimeHour: Int,
        val endTimeMinute: Int
)

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM Lesson")
    fun getAllLessons():List<Lesson>

    @Insert
    fun insert(lesson: Lesson)
}