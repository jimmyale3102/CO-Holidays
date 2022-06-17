package dev.alejo.colombian_holidays.data.network

import dev.alejo.colombian_holidays.core.Constants
import dev.alejo.colombian_holidays.data.model.HolidayModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HolidayApiClient {

    @GET("IsTodayPublicHoliday/${Constants.COLOMBIA_CODE}")
    suspend fun getTodayHoliday(): Response<Void>

    @GET("PublicHolidays/{year}/${Constants.COLOMBIA_CODE}")
    suspend fun getHolidaysByYear(
        @Path("year") year: String
    ): Response<List<HolidayModel>>

    @GET("NextPublicHolidays/${Constants.COLOMBIA_CODE}")
    suspend fun getNextPublicHoliday(): Response<List<HolidayModel>>

}