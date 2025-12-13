package com.example.mypage.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mypage.MainActivity
import com.example.mypage.R

const val BOOKS_CHANNEL_ID = "books_updates_channel"

fun showNewBooksNotification(
    context: Context,
    category: String,
    count: Int
) {
    val notificationId = 1001

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("openCategory", category)
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val title = "Nouveaux livres $category"
    val text = "De nouveaux livres de $category ont été ajoutés."

    val builder = NotificationCompat.Builder(context, BOOKS_CHANNEL_ID)
        .setSmallIcon(R.drawable.cover4)
        .setContentTitle(title)
        .setContentText(text)
        .setStyle(NotificationCompat.BigTextStyle().bigText(text))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    val notificationManager = NotificationManagerCompat.from(context)

    //  Vérification de la permission avant d'envoyer la notif
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        notificationManager.notify(notificationId, builder.build())
    }
    // sinon, on ne notifie pas (ou tu peux log / ignorer)
}
