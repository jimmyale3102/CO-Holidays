package dev.alejo.colombian_holidays.ui.viewmodel

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.core.Constants
import dev.alejo.colombian_holidays.core.Constants.Companion.MESSAGE_EXTRA
import dev.alejo.colombian_holidays.core.Constants.Companion.NOTIFICATION_ID_EXTRA
import dev.alejo.colombian_holidays.core.Notification
import dev.alejo.colombian_holidays.domain.GetHolidayNotificationUseCase
import dev.alejo.colombian_holidays.domain.InsertHolidayNotificationUseCase
import dev.alejo.colombian_holidays.domain.RemoveHolidayNotificationUseCase
import dev.alejo.colombian_holidays.domain.model.HolidayNotificationItem
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HolidayDetailViewModel @Inject constructor(
    private val getHolidaysNotificationsUseCase: GetHolidayNotificationUseCase,
    private val insertHolidayNotificationUseCase: InsertHolidayNotificationUseCase,
    private val removeHolidayNotificationUseCase: RemoveHolidayNotificationUseCase
): ViewModel() {

    val holidayNotificationResponse = MutableLiveData<HolidayNotificationItem?>()
    val existHolidayNotification = MutableLiveData<Boolean>(false)

    fun onCreate(context: Context, holidayNotificationId: Int) {
        createNotificationChannel(context)
        getHolidaysNotifications(holidayNotificationId)
    }

    private fun getHolidaysNotifications(holidayNotificationId: Int) {
        viewModelScope.launch {
            val holidayNotification = getHolidaysNotificationsUseCase(holidayNotificationId)
            holidayNotificationResponse.postValue(holidayNotification)
            existHolidayNotification.postValue(holidayNotification != null)
        }
    }

    fun insertHolidayNotification(holidayNotification: HolidayNotificationItem) {
        viewModelScope.launch {
            insertHolidayNotificationUseCase(holidayNotification)
            existHolidayNotification.postValue(true)
        }
    }

    fun removeHolidayNotification(holidayNotificationId: Int) {
        viewModelScope.launch {
            removeHolidayNotificationUseCase(holidayNotificationId)
            existHolidayNotification.postValue(false)
        }
    }

    private fun createNotificationChannel(context: Context) {
        viewModelScope.launch {
            val name = "Notification Channel"
            val description = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance).apply {
                this.description = description
                setSound(
                    Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notification_sound),
                    audioAttributes
                )
            }
            val notificationManager =
                context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(context: Context, notificationId: Int, message: String, time: Long) {
        viewModelScope.launch {
            val intent = Intent(context, Notification::class.java).apply {
                putExtra(MESSAGE_EXTRA, message)
                putExtra(NOTIFICATION_ID_EXTRA, notificationId)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
    }
    //System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)

    fun removeNotification(context: Context, notificationId: Int) {
        viewModelScope.launch {
            val intent = Intent(context, Notification::class.java).apply {
                putExtra(MESSAGE_EXTRA, "asd")
                putExtra(NOTIFICATION_ID_EXTRA, notificationId)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }
}