package dev.alejo.colombiaholidays.core

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import dev.alejo.colombiaholidays.R
import dev.alejo.colombiaholidays.core.Constants.Companion.CHANNEL_ID
import dev.alejo.colombiaholidays.core.Constants.Companion.MESSAGE_EXTRA
import dev.alejo.colombiaholidays.core.Constants.Companion.NOTIFICATION_ID

class Notification: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(intent?.getStringExtra(MESSAGE_EXTRA))
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}