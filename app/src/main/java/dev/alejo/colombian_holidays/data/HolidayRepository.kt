package dev.alejo.colombian_holidays.data

import dev.alejo.colombian_holidays.core.extensions.toDatabase
import dev.alejo.colombian_holidays.core.extensions.toDomain
import dev.alejo.colombian_holidays.data.database.dao.HolidayNotificationDao
import dev.alejo.colombian_holidays.data.model.HolidayModel
import dev.alejo.colombian_holidays.data.network.HolidayService
import dev.alejo.colombian_holidays.domain.model.HolidayNotificationItem
import javax.inject.Inject

class HolidayRepository @Inject constructor(
    private val service: HolidayService,
    private val dao: HolidayNotificationDao
) {

    suspend fun getHolidaysByYear(year: String): List<HolidayModel> = service.getHolidaysByYear(year)

    suspend fun getNextPublicHoliday(): List<HolidayModel> = service.getNextPublicaHoliday()

    suspend fun getTodayHoliday(): Int = service.getTodayHoliday()

    suspend fun getHolidayNotification(holidayNotificationId: Int): HolidayNotificationItem? =
        dao.getHolidayNotification(holidayNotificationId)?.toDomain()

    suspend fun insertHolidayNotification(holidayNotification: HolidayNotificationItem) {
        dao.insertHolidayNotification(holidayNotification.toDatabase())
    }

    suspend fun removeHolidayNotification(holidayNotificationId: Int) {
        dao.removeHolidayNotification(holidayNotificationId)
    }
}