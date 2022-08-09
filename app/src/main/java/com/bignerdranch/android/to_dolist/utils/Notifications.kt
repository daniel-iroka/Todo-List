package com.bignerdranch.android.to_dolist.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.bignerdranch.android.to_dolist.MainActivity
import com.bignerdranch.android.to_dolist.R

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "Channel1"
const val TITLE_EXTRA = "titleExtra"

class Notifications : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        val notification  = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_icon)
            .setContentTitle(intent.getStringExtra(TITLE_EXTRA))
            .setAutoCancel(true)
            .build()

        // Registering our channel with the system
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}