package notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

//class NotificationReceiver : BroadcastReceiver() {
//    private val setAlarm = SetAlarm()
//    override fun onReceive(context: Context?, intent: Intent?) {
//        val serviceIntent = Intent(context, NotificationService::class.java)
//        context?.startService(serviceIntent)
//        context?.let { setAlarm.setAlarm(it) }
//    }
//}