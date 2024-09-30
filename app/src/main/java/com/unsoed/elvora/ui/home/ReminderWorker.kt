package com.unsoed.elvora.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.unsoed.elvora.R
import com.unsoed.elvora.data.network.ApiConfig

class ReminderWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {
    private var resultStatus: Result? = null
    override fun doWork(): Result {
        val token = inputData.getString(EXTRA_TOKEN)
        Log.d(TAG, token.toString())
        return fetchEventData(token!!)
    }

    private fun fetchEventData(token: String): Result {
        Log.d(TAG, "getEvent: Mulai.....")
        try {
            val response = ApiConfig.getApiService().getDashboardV2(token = "Bearer $token").execute()
            if (response.isSuccessful){
                val responseBody = response.body()
                if(responseBody != null) {
                    val batteryData = responseBody.data?.battery
                    val transactionData = responseBody.data?.transaction
                    if(batteryData != null) {
                        resultStatus = Result.success()
                        showNotification("${transactionData?.batteryName} Subscription", "Your subscription for your (${transactionData?.batteryName}) battery will expire in ${batteryData.remainingTime} days.")
                        Log.d(TAG, "onSuccess: Selesai.....")
                    } else {
                        resultStatus = Result.failure()
                        Log.d(TAG, "onSuccess: kosong.....")
                    }
                }
            } else {
                showNotification("Data baterai tidak berhasil didapatkan", response.body()?.message.toString())
                Log.d(TAG, "onSuccess: Gagal.....")
                resultStatus = Result.failure()
            }
        } catch (e: Exception) {
            showNotification("Data baterai gagal diambil", e.message.toString())
            Log.d(TAG, "onSuccess: Gagal.....")
            resultStatus = Result.failure()
        }

        return resultStatus as Result
    }

    private fun showNotification(title: String, content: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_battery)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSubText("Subscription Reminder")
            .setAutoCancel(true)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val TAG = "ReminderWorker"
        const val NOTIFICATION_ID = 120
        const val EXTRA_TOKEN = "extra_token"
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "elvora channel"
    }
}