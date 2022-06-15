package dev.alejo.colombian_holidays.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.alejo.colombian_holidays.data.database.HolidayNotificationDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val HOLIDAY_NOTIFICATION_DATABASE_NAME = "holiday_notification_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            HolidayNotificationDatabase::class.java,
            HOLIDAY_NOTIFICATION_DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideHolidayNotificationDao(database: HolidayNotificationDatabase) =
        database.holidayNotificationDao()

}