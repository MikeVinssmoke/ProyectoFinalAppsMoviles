package com.example.romero.proyectofinalappsmoviles.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_events")
data class SyncEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reportId: Long,
    val action: String,   // CREATE, UPDATE, DELETE
    val pendingAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)