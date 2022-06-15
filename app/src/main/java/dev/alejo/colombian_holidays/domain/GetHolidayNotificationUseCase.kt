package dev.alejo.colombian_holidays.domain

import dev.alejo.colombian_holidays.data.HolidayRepository
import dev.alejo.colombian_holidays.domain.model.HolidayNotificationItem
import javax.inject.Inject

class GetHolidayNotificationUseCase @Inject constructor(
    private val repository: HolidayRepository
){
    suspend operator fun invoke(holidayNotificationId: Int): HolidayNotificationItem? =
        repository.getHolidayNotification(holidayNotificationId)
}