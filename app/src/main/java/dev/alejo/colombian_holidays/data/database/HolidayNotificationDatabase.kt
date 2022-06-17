package dev.alejo.colombian_holidays.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.alejo.colombian_holidays.data.database.dao.HolidayNotificationDao
import dev.alejo.colombian_holidays.data.database.entities.HolidayNotificationEntity

@Database(entities = [HolidayNotificationEntity::class], version = 1)
abstract class HolidayNotificationDatabase: RoomDatabase() {
    abstract fun holidayNotificationDao(): HolidayNotificationDao
}