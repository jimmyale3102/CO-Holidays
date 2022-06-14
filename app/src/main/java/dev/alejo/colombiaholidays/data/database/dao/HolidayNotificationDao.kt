package dev.alejo.colombiaholidays.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.alejo.colombiaholidays.data.database.entities.HolidayNotificationEntity

@Dao
interface HolidayNotificationDao {

    @Query("SELECT * FROM holiday_notification WHERE id = :id")
    suspend fun getHolidayNotification(id: Int): HolidayNotificationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHolidayNotification(holidayNotification: HolidayNotificationEntity)

    @Query("DELETE FROM holiday_notification WHERE id = :id")
    suspend fun removeHolidayNotification(id: Int)

}