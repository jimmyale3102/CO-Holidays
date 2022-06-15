package dev.alejo.colombiaholidays.core

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import dev.alejo.colombiaholidays.core.Constants.Companion.CHANNEL_ID
import dev.alejo.colombiaholidays.core.Constants.Companion.MESSAGE_EXTRA
import dev.alejo.colombiaholidays.core.Constants.Companion.NOTIFICATION_ID_EXTRA
import dev.alejo.colombiaholidays.core.Constants.Companion.NOTIFICATION_TITLE
import dev.alejo.colombiaholidays.ui.view.MainActivity
import dev.alejo.colombiaholidays.ui.viewmodel.HolidayDetailViewModel

class Notification: BroadcastReceiver() {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(intent?.getStringExtra(MESSAGE_EXTRA))
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val homeIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(NOTIFICATION_ID_EXTRA, intent?.getIntExtra(NOTIFICATION_ID_EXTRA, 0))
            Intent.FLAG_ACTIVITY_SINGLE_TOP
            Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            homeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        notification.contentIntent = pendingIntent

        manager.notify(
            intent?.getIntExtra(NOTIFICATION_ID_EXTRA, 0) ?: 0,
            notification
        )

    }
}