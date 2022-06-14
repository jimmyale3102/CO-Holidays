package dev.alejo.colombiaholidays.domain

import dev.alejo.colombiaholidays.data.HolidayRepository
import dev.alejo.colombiaholidays.domain.model.HolidayNotificationItem
import javax.inject.Inject

class InsertHolidayNotificationUseCase @Inject constructor(
    private val repository: HolidayRepository
){
    suspend operator fun invoke(holidayNotification: HolidayNotificationItem) {
        repository.insertHolidayNotification(holidayNotification)
    }
}