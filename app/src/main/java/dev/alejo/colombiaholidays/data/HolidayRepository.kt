package dev.alejo.colombiaholidays.data

import dev.alejo.colombiaholidays.core.extensions.toDatabase
import dev.alejo.colombiaholidays.core.extensions.toDomain
import dev.alejo.colombiaholidays.data.database.dao.HolidayNotificationDao
import dev.alejo.colombiaholidays.data.database.entities.HolidayNotificationEntity
import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.data.network.HolidayService
import dev.alejo.colombiaholidays.domain.model.HolidayNotificationItem
import javax.inject.Inject

class HolidayRepository @Inject constructor(
    private val service: HolidayService,
    private val dao: HolidayNotificationDao
) {

    suspend fun getHolidaysByYear(year: String): List<HolidayModel> = service.getHolidaysByYear(year)

    suspend fun getNextPublicHoliday(): List<HolidayModel> = service.getNextPublicaHoliday()

    suspend fun getTodayHoliday(): String = service.getTodayHoliday()

    suspend fun getHolidayNotification(holidayNotificationId: Int): HolidayNotificationItem? =
        dao.getHolidayNotification(holidayNotificationId)?.toDomain()

    suspend fun insertHolidayNotification(holidayNotification: HolidayNotificationItem) {
        dao.insertHolidayNotification(holidayNotification.toDatabase())
    }

    suspend fun removeHolidayNotification(holidayNotificationId: Int) {
        dao.removeHolidayNotification(holidayNotificationId)
    }
}