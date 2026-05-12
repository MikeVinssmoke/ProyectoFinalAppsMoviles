package com.example.romero.proyectofinalappsmoviles.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.romero.proyectofinalappsmoviles.data.local.AppDatabase
import com.example.romero.proyectofinalappsmoviles.data.local.entity.ReminderEntity
import com.example.romero.proyectofinalappsmoviles.receiver.ReminderReceiver
import com.example.romero.proyectofinalappsmoviles.util.Constants

class ReminderRepository(private val db: AppDatabase, private val context: Context) {

    fun getActiveReminders()              = db.reminderDao().getActiveReminders()
    fun getRemindersByReport(id: Long)    = db.reminderDao().getRemindersByReport(id)
    suspend fun getActiveRemindersList()  = db.reminderDao().getActiveRemindersList()

    suspend fun scheduleReminder(reminder: ReminderEntity): Long {
        val id = db.reminderDao().insert(reminder)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = pendingIntent(id, reminder.message)
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.triggerAt, pi)
        return id
    }

    suspend fun cancelReminder(reminderId: Long) {
        db.reminderDao().cancelReminder(reminderId)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pendingIntent(reminderId, ""))
    }

    private fun pendingIntent(reminderId: Long, message: String): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(Constants.EXTRA_REMINDER_ID,  reminderId)
            putExtra(Constants.EXTRA_REMINDER_MSG, message)
        }
        return PendingIntent.getBroadcast(
            context, reminderId.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}