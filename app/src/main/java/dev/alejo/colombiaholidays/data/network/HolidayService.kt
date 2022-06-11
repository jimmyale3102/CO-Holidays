package dev.alejo.colombiaholidays.data.network

import dev.alejo.colombiaholidays.data.model.HolidayModel
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

}