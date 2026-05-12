package com.example.romero.proyectofinalappsmoviles.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.romero.proyectofinalappsmoviles.data.local.AppDatabase
import com.example.romero.proyectofinalappsmoviles.repository.ReportRepository
import com.example.romero.proyectofinalappsmoviles.util.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (NetworkUtils.isOnline(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                ReportRepository(AppDatabase.getInstance(context)).syncWithServer()
            }
        }
    }
}