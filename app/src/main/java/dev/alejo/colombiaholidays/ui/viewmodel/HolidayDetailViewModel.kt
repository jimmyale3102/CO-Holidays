package dev.alejo.colombiaholidays.ui.viewmodel

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alejo.colombiaholidays.core.Constants
import dev.alejo.colombiaholidays.core.Constants.Companion.MESSAGE_EXTRA
import dev.alejo.colombiaholidays.core.Constants.Companion.NOTIFICATION_ID
import dev.alejo.colombiaholidays.core.Constants.Companion.NOTIFICATION_ID_EXTRA
import dev.alejo.colombiaholidays.core.Notification
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

//@HiltViewModel
class HolidayDetailViewModel: ViewModel() {

    fun onCreate(context: Context) {
        createNotificationChannel(context)
    }

    private fun createNotificationChannel(context: Context) {
        viewModelScope.launch {
            val name = "Notification Channel"
            val description = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance).apply {
                this.description = description
            }
            val notificationManager =
                context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(context: Context, notificationId: Int, message: String) {
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
                System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1),
                pendingIntent
            )
        }
    }
}