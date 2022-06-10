package dev.alejo.colombiaholidays.data.network

import dev.alejo.colombiaholidays.core.Constants
import dev.alejo.colombiaholidays.data.model.HolidayModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HolidayApiClient {

    @GET("PublicHolidays/{year}/${Constants.COLOMBIA_CODE}")
    suspend fun getHolidaysByYear(
        @Path("year") year: String
    ): Response<List<HolidayModel>>

}