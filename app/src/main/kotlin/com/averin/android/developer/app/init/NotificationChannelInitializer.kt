package com.averin.android.developer.app.init

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.multidex.MultiDexApplication
import com.averin.android.developer.R
import com.averin.android.developer.base.init.BaseAppInitializer

class NotificationChannelInitializer(private val application: Application) : BaseAppInitializer {
    override fun onAppCreateInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = application.getString(R.string.notification_channel_name)
            val descriptionText = application.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("NAVIGATION_CHANNEL_ID", name, importance)
            channel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = application
                .getSystemService(MultiDexApplication.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
