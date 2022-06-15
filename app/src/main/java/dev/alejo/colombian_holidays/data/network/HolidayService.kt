package dev.alejo.colombian_holidays.data.network

import dev.alejo.colombian_holidays.core.Constants.Companion.CODE_200
import dev.alejo.colombian_holidays.core.Constants.Companion.CODE_200_RESPONSE
import dev.alejo.colombian_holidays.core.Constants.Companion.CODE_204
import dev.alejo.colombian_holidays.core.Constants.Companion.CODE_204_RESPONSE
import dev.alejo.colombian_holidays.core.Constants.Companion.CODE_400_RESPONSE
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

    suspend fun getTodayHoliday(): String {
        return withContext(Dispatchers.IO) {
            val response = api.getTodayHoliday()
            when(response.code()) {
                CODE_200 -> CODE_200_RESPONSE
                CODE_204 -> CODE_204_RESPONSE
                else -> CODE_400_RESPONSE
            }
        }
    }

}