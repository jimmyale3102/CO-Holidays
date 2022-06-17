package dev.alejo.colombian_holidays.domain

import dev.alejo.colombian_holidays.data.HolidayRepository
import javax.inject.Inject

class GetTodayHolidayUseCase @Inject constructor(
    private val repository: HolidayRepository
) {
    suspend operator fun invoke(): Int = repository.getTodayHoliday()
}