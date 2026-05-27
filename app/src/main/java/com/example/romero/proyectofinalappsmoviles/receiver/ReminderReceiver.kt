package com.example.romero.proyectofinalappsmoviles.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.romero.proyectofinalappsmoviles.util.Constants
import com.example.romero.proyectofinalappsmoviles.util.NotificationHelper

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        android.util.Log.d("ReminderReceiver", "Alarma recibida!")
        val id  = intent.getLongExtra(Constants.EXTRA_REMINDER_ID, -1)
        val msg = intent.getStringExtra(Constants.EXTRA_REMINDER_MSG) ?: "Recordatorio pendiente"
        NotificationHelper.showNotification(context, id.toInt(), "Nodo Cívico", msg)
    }
}