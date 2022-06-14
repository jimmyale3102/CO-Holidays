package dev.alejo.colombiaholidays.data

import dev.alejo.colombiaholidays.data.database.dao.HolidayNotificationDao
import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.data.network.HolidayService
import javax.inject.Inject

class HolidayRepository @Inject constructor(
    private val service: HolidayService,
    private val dao: HolidayNotificationDao
) {
    suspend fun getHolidaysByYear(year: String): List<HolidayModel> = service.getHolidaysByYear(year)

    suspend fun getNextPublicHoliday(): List<HolidayModel> = service.getNextPublicaHoliday()

    suspend fun getTodayHoliday(): String = service.getTodayHoliday()
}