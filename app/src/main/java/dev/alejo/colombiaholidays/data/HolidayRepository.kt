package dev.alejo.colombiaholidays.data

import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.data.network.HolidayService
import javax.inject.Inject

class HolidayRepository @Inject constructor(
    private val service: HolidayService
) {
    suspend fun getHolidaysByYear(year: String): List<HolidayModel> = service.getHolidaysByYear(year)
}