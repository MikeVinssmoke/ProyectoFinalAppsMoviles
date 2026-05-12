package com.example.romero.proyectofinalappsmoviles.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.romero.proyectofinalappsmoviles.data.local.entity.ReminderEntity

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders WHERE isActive = 1 ORDER BY triggerAt ASC")
    fun getActiveReminders(): LiveData<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE reportId = :reportId")
    fun getRemindersByReport(reportId: Long): LiveData<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE isActive = 1")
    suspend fun getActiveRemindersList(): List<ReminderEntity>

    @Insert
    suspend fun insert(reminder: ReminderEntity): Long

    @Query("UPDATE reminders SET isActive = 0 WHERE id = :id")
    suspend fun cancelReminder(id: Long)

    @Delete
    suspend fun delete(reminder: ReminderEntity)
}