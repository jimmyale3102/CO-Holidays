package dev.alejo.colombiaholidays.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holiday_notification")
data class HolidayNotificationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idGenerated") val idGenerated: Int = 0,
    @ColumnInfo(name = "id") val id: Int
)