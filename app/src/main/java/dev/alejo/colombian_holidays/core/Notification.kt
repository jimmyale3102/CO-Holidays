package dev.alejo.colombian_holidays.core

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.core.Constants.Companion.CHANNEL_ID
import dev.alejo.colombian_holidays.core.Constants.Companion.MESSAGE_EXTRA
import dev.alejo.colombian_holidays.core.Constants.Companion.NOTIFICATION_ID_EXTRA
import dev.alejo.colombian_holidays.core.Constants.Companion.NOTIFICATION_TITLE
import dev.alejo.colombian_holidays.ui.view.MainActivity

class Notification: BroadcastReceiver() {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.splash_icon)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(intent?.getStringExtra(MESSAGE_EXTRA))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(
                Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notification_sound)
            )
            .setAutoCancel(true)
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