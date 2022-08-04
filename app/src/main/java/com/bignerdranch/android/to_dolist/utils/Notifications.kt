package com.bignerdranch.android.to_dolist.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.bignerdranch.android.to_dolist.R

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "Channel1"
const val TITLE_EXTRA = "titleExtra"
const val MESSAGE_EXTRA = "messageExtra"

class Notifications : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val notification  = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_icon)
            .setContentTitle(intent.getStringExtra(TITLE_EXTRA))
            .setContentText(MESSAGE_EXTRA)
            .build()

        // Registering our channel with the system
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}