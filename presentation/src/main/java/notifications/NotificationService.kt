package notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.githubstars.R
import data.repository.DataRepository
import kotlinx.coroutines.*
import ui.graph_screen.GraphActivity
import ui.repo_screen.RepoActivity
import kotlin.coroutines.suspendCoroutine

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    companion object {
        private const val CHANNEL_ID = "GitHubStars Alert Notification ID"
        private const val CHANNEL_NAME = "GitHubStars Alert Notification"
    }
    private val dataRepository = DataRepository

    override fun doWork(): Result {
        Log.d("WORK", "STARTED")
        val notificationsList = runBlocking(Dispatchers.IO) {
            dataRepository.getNotificationsData(applicationContext)
        }
        notificationsList.forEach {
            if (it.additionalStars != 0) {
                val repoName = it.repoName
                val additionalStars = it.additionalStars
                val notificationId = notificationsList.indexOf(it) + 1
                val contentText = if (additionalStars > 0) {
                    "${applicationContext.getString(R.string.notification_stars_added)} $additionalStars"
                } else {
                    "${applicationContext.getString(R.string.notification_stars_decreased)} $additionalStars ((("
                }
                val pendingIntent = PendingIntent.getActivities(
                    applicationContext,
                    notificationId,
                    arrayOf(
                        RepoActivity.createIntent(applicationContext),
                        GraphActivity.createIntent(
                            applicationContext,
                            it.userName,
                            it.repoName,
                            it.stargazersCount
                        )
                    ),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val notification =
                    getNotificationBuilder(
                        repoName,
                        contentText,
                        pendingIntent
                    ).build()
                setForegroundAsync(createForegroundInfo(notificationId, notification))
                getManager().notify(notificationId, notification)

            }
        }
        return Result.success()
    }

    private fun createForegroundInfo(notificationId: Int, notification: Notification): ForegroundInfo {
        return ForegroundInfo(notificationId, notification)
    }

    private fun createChannels() {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(true)
        getManager().createNotificationChannel(channel)
    }

    private fun getManager(): NotificationManager {
        return applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun getNotificationBuilder(
        repoName: String,
        contentText: String,
        pendingIntent: PendingIntent,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(repoName)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.full_star_icon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }
}

//class NotificationService() : Service() {
//
//    companion object {
//        private const val CHANNEL_ID = "GitHubStars Alert Notification ID"
//        private const val CHANNEL_NAME = "GitHubStars Alert Notification"
//    }
//
//    private var manager: NotificationManager? = null
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        createChannels()
//        val dataRepository = DataRepository
//        val notificationsList = runBlocking(Dispatchers.IO) {
//            dataRepository.getNotificationsData(this@NotificationService)
//        }
//        notificationsList.forEach {
//            if (it.additionalStars != 0) {
//                val repoName = it.repoName
//                val additionalStars = it.additionalStars
//                val notificationId = notificationsList.indexOf(it) + 1
//                val contentText = if (additionalStars > 0) {
//                    "${getString(R.string.notification_stars_added)} $additionalStars"
//                } else {
//                    "${getString(R.string.notification_stars_decreased)} $additionalStars ((("
//                }
//                val pendingIntent = PendingIntent.getActivities(
//                    this,
//                    notificationId,
//                    arrayOf(
//                        RepoActivity.createIntent(this),
//                        GraphActivity.createIntent(
//                            this,
//                            it.userName,
//                            it.repoName,
//                            it.stargazersCount
//                        )
//                    ),
//                    PendingIntent.FLAG_UPDATE_CURRENT
//                )
//                val notification =
//                    getNotificationBuilder(
//                        repoName,
//                        contentText,
//                        pendingIntent
//                    ).build()
//                getManager().notify(notificationId, notification)
//            }
//        }
//        return START_STICKY
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    private fun createChannels() {
//        val channel =
//            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
//        channel.enableVibration(true)
//        getManager().createNotificationChannel(channel)
//    }
//
//    private fun getManager(): NotificationManager {
//        if (manager == null) {
//            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        }
//        return manager as NotificationManager
//    }
//
//    private fun getNotificationBuilder(
//        repoName: String,
//        contentText: String,
//        pendingIntent: PendingIntent,
//    ): NotificationCompat.Builder {
//        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
//            .setContentTitle(repoName)
//            .setContentText(contentText)
//            .setSmallIcon(R.drawable.full_star_icon)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//    }
//}
