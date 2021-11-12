package com.dicoding.todoapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.ui.detail.DetailTaskActivity
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    private lateinit var taskRepository: TaskRepository
    private val channelName = inputData.getString("0")

    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }

        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

    }

    override fun doWork(): Result {
        taskRepository = TaskRepository.getInstance(applicationContext)
        //TODO 14 : If notification preference on, get nearest active task from repository and show notification with pending intent
        val notification = channelName?.let {
            NotificationCompat.Builder(
                applicationContext,
                    it
        )
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(taskRepository.getNearestActiveTask().title)
                .setContentText(DateConverter.convertMillisToString(taskRepository.getNearestActiveTask().dueDateMillis))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentIntent(getPendingIntent(taskRepository.getNearestActiveTask()))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelName2 = "Channel Name"
            val channelDescription = "Channel Description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelName, channelName2, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager = applicationContext.getSystemService(
                    Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }


        with(NotificationManagerCompat.from(applicationContext)) {
            if (notification != null) {
                notify(0, notification.build())
            }
        }




        return Result.success()
    }

}
