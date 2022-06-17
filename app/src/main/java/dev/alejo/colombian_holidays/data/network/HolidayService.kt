package dev.alejo.colombian_holidays.data.network

import dev.alejo.colombian_holidays.data.model.HolidayModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HolidayService @Inject constructor(
    private val api: HolidayApiClient
) {

    suspend fun getHolidaysByYear(year: String): List<HolidayModel> {
        return withContext(Dispatchers.IO) {
            val response = api.getHolidaysByYear(year)
            response.body() ?: emptyList()
        }
    }

    suspend fun getNextPublicaHoliday(): List<HolidayModel> {
        return withContext(Dispatchers.IO) {
            val response = api.getNextPublicHoliday()
            response.body() ?: emptyList()
        }
    }

    suspend fun getTodayHoliday(): Int {
        return withContext(Dispatchers.IO) {
            val response = api.getTodayHoliday()
            response.code()
        }
    }

}