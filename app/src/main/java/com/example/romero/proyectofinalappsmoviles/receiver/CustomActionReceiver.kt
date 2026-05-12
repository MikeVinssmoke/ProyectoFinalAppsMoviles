package com.example.romero.proyectofinalappsmoviles.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.romero.proyectofinalappsmoviles.data.local.AppDatabase
import com.example.romero.proyectofinalappsmoviles.repository.ReportRepository
import com.example.romero.proyectofinalappsmoviles.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val db = AppDatabase.getInstance(context)
        when (intent.action) {
            Constants.ACTION_SYNC_MANUAL   ->
                CoroutineScope(Dispatchers.IO).launch { ReportRepository(db).syncWithServer() }
            Constants.ACTION_CLEAR_PENDING ->
                CoroutineScope(Dispatchers.IO).launch { db.syncEventDao().clearSynced() }
        }
    }
}