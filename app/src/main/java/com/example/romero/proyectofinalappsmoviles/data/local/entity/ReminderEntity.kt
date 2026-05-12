package com.example.romero.proyectofinalappsmoviles.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reportId: Long,
    val triggerAt: Long,
    val message: String,
    val isActive: Boolean = true
)