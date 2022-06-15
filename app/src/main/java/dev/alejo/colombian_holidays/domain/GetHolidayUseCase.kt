package dev.alejo.colombian_holidays.domain

import dev.alejo.colombian_holidays.data.HolidayRepository
import dev.alejo.colombian_holidays.data.model.HolidayModel
import javax.inject.Inject

class GetHolidayUseCase @Inject constructor(
    private val repository: HolidayRepository
) {
    suspend operator fun invoke(year: String): List<HolidayModel> = repository.getHolidaysByYear(year)
}