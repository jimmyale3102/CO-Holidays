package dev.alejo.colombian_holidays.domain

import dev.alejo.colombian_holidays.data.HolidayRepository
import javax.inject.Inject

class RemoveHolidayNotificationUseCase @Inject constructor(
    private val repository: HolidayRepository
){
    suspend operator fun invoke(holidayNotificationId: Int) {
        repository.removeHolidayNotification(holidayNotificationId)
    }
}