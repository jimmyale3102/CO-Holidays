package dev.alejo.colombiaholidays.domain

import dev.alejo.colombiaholidays.data.HolidayRepository
import dev.alejo.colombiaholidays.domain.model.HolidayNotificationItem
import javax.inject.Inject

class RemoveHolidayNotificationUseCase @Inject constructor(
    private val repository: HolidayRepository
){
    suspend operator fun invoke(holidayNotificationId: Int) {
        repository.removeHolidayNotification(holidayNotificationId)
    }
}