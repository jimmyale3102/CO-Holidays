package dev.alejo.colombiaholidays.domain

import dev.alejo.colombiaholidays.data.HolidayRepository
import javax.inject.Inject

class GetTodayHolidayUseCase @Inject constructor(
    private val repository: HolidayRepository
) {
    suspend operator fun invoke(): String = repository.getTodayHoliday()
}