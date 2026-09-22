package com.example.esp32notif

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotifListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        try {
            val pkg = sbn.packageName ?: return
            val extras = sbn.notification.extras
            val title = extras.getCharSequence("android.title")?.toString() ?: ""
            val text = extras.getCharSequence("android.text")?.toString() ?: ""
            if (title.isEmpty() && text.isEmpty()) return

            val appName = packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(pkg, 0)
            ).toString()

            val line = "$appName : $title - $text\n"
            Log.d("NotifListener", line)

            if (!BtManager.isConnected()) {
                BtManager.connect(applicationContext)
            }
            BtManager.send(line)
        } catch (e: Exception) {
            Log.e("NotifListener", "Erreur: ${e.message}")
        }
    }
}
