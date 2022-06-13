package dev.alejo.colombiaholidays.domain

import dev.alejo.colombiaholidays.data.HolidayRepository
import dev.alejo.colombiaholidays.data.model.HolidayModel
import javax.inject.Inject

class GetNextPublicHolidayUserCase @Inject constructor(
    private val repository: HolidayRepository
) {
    suspend operator fun invoke(): List<HolidayModel> = repository.getNextPublicHoliday()
}