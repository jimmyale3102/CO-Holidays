package dev.alejo.colombiaholidays.core.extensions

import dev.alejo.colombiaholidays.data.database.entities.HolidayNotificationEntity
import dev.alejo.colombiaholidays.domain.model.HolidayNotificationItem

fun HolidayNotificationItem.toDatabase() = HolidayNotificationEntity(id = id)