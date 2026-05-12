package com.example.romero.proyectofinalappsmoviles.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.romero.proyectofinalappsmoviles.data.local.AppDatabase
import com.example.romero.proyectofinalappsmoviles.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        CoroutineScope(Dispatchers.IO).launch {
            val reminders = AppDatabase.getInstance(context).reminderDao().getActiveRemindersList()
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val now = System.currentTimeMillis()
            for (r in reminders) {
                if (r.triggerAt > now) {
                    val pi = PendingIntent.getBroadcast(
                        context, r.id.toInt(),
                        Intent(context, ReminderReceiver::class.java).apply {
                            putExtra(Constants.EXTRA_REMINDER_ID,  r.id)
                            putExtra(Constants.EXTRA_REMINDER_MSG, r.message)
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, r.triggerAt, pi)
                }
            }
        }
    }
}