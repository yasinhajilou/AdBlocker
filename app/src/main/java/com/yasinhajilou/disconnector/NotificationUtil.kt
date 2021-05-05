package com.yasinhajilou.disconnector

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationUtil(private val context: Context) {
    private val CHANNEL_ID = "110"
    private val NOTIFICATION_ID = 125
    public fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val notifChannel = NotificationChannel(
                CHANNEL_ID,
                "Disconnect Vpn",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "This is shortcut to disconnect VPN with out showing ad!"
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notifChannel)
        }
    }

    public fun showNotification() {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("KEY", 100)
        val pendingIntent = PendingIntent.getBroadcast(context, 1254, intent, 0)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_security_off)
            .setContentTitle("Disconnect VPN")
            .setContentText("Tap on disconnect button to disconnect your vpn without any ad!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .addAction(R.drawable.ic_baseline_close_24, "Disconnect", pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    public fun removeNotification() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }
}