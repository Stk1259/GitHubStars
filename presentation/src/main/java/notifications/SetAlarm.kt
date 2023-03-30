package notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import data.repository.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class SetAlarm {
    companion object {
        private const val CHANNEL_NAME = "GitHubStars Alert Notification"
    }
    private val dataRepository = DataRepository
    fun setAlarm(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            if (dataRepository.checkFavouriteRepos(context)) {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val notificationWorkerRequest =
                    PeriodicWorkRequestBuilder<NotificationWorker>(2, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .setInitialDelay(2, TimeUnit.MINUTES)
                        .build()

                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    CHANNEL_NAME,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    notificationWorkerRequest
                )
            }
        }
    }
    private fun getInitialDelay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        val targetTime = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 19)
        val currentTime = calendar.timeInMillis
        return targetTime - currentTime
    }
}
//    private fun startAlarmMorning(context: Context) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, NotificationReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, 9)
//        val currentTime = System.currentTimeMillis()
//        if (currentTime > calendar.timeInMillis) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1)
//        }
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            pendingIntent
//        )
//    }
//
//    private fun startAlarmEvening(context: Context) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, NotificationReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, 19)
//        val currentTime = System.currentTimeMillis()
//        if (currentTime > calendar.timeInMillis) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1)
//        }
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            pendingIntent
//        )
//    }
