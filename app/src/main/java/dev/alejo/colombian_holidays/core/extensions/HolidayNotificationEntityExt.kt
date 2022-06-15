package dev.alejo.colombian_holidays.core.extensions

import dev.alejo.colombian_holidays.data.database.entities.HolidayNotificationEntity
import dev.alejo.colombian_holidays.domain.model.HolidayNotificationItem

fun HolidayNotificationEntity.toDomain() = HolidayNotificationItem(id)