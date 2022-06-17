package dev.alejo.colombian_holidays.domain

import dev.alejo.colombian_holidays.data.HolidayRepository
import dev.alejo.colombian_holidays.domain.model.HolidayNotificationItem
import javax.inject.Inject

class InsertHolidayNotificationUseCase @Inject constructor(
    private val repository: HolidayRepository
){
    suspend operator fun invoke(holidayNotification: HolidayNotificationItem) {
        repository.insertHolidayNotification(holidayNotification)
    }
}