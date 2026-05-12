package com.example.romero.proyectofinalappsmoviles.repository

import com.example.romero.proyectofinalappsmoviles.data.local.AppDatabase

class SyncEventRepository(private val db: AppDatabase) {
    val pendingEvents = db.syncEventDao().getPendingEvents()
    val pendingCount  = db.syncEventDao().getPendingCount()

    suspend fun getPendingList() = db.syncEventDao().getPendingEventsList()
    suspend fun markSynced(id: Long) = db.syncEventDao().markSynced(id)
    suspend fun clearSynced()        = db.syncEventDao().clearSynced()
}