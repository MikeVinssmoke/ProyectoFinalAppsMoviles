package com.example.romero.proyectofinalappsmoviles.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val status: String = "ABIERTO",
    val location: String,
    val date: Long = System.currentTimeMillis(),
    val evidenceUri: String? = null,
    val isSynced: Boolean = false,
    val serverId: Long? = null
)