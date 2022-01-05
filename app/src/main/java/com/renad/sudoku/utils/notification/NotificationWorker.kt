package com.renad.sudoku.utils.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(private val context: Context, workParams: WorkerParameters) : Worker(context, workParams) {
    override fun doWork(): Result {
        val notification = NotificationCompat
            .Builder(context, "NOTIFICATION_CHANNEL_ID").setTicker("SUDOKU_NOTIFICATION")
                .setSmallIcon(android.R.drawable.btn_star_big_on)
                .setContentTitle("Sudoku")
                .setContentText("Let's play now !!")
                .setAutoCancel(true)//when we click it, it gets dismissed
                .build()
        //create notification manager
        val notificationManager = NotificationManagerCompat.from(context)

        //call notification manager
        notificationManager.notify(0, notification)
        return Result.success()
    }
}