package dev.alejo.colombian_holidays.core.extensions

import dev.alejo.colombian_holidays.data.database.entities.HolidayNotificationEntity
import dev.alejo.colombian_holidays.domain.model.HolidayNotificationItem

fun HolidayNotificationItem.toDatabase() = HolidayNotificationEntity(id = id)