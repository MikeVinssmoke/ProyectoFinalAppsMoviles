package com.example.romero.proyectofinalappsmoviles.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.romero.proyectofinalappsmoviles.data.local.AppDatabase
import com.example.romero.proyectofinalappsmoviles.data.local.entity.ReminderEntity
import com.example.romero.proyectofinalappsmoviles.repository.ReminderRepository
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ReminderRepository(AppDatabase.getInstance(application), application)

    val activeReminders = repo.getActiveReminders()

    fun getRemindersForReport(reportId: Long) = repo.getRemindersByReport(reportId)

    fun scheduleReminder(reportId: Long, triggerAt: Long, message: String) {
        viewModelScope.launch {
            repo.scheduleReminder(ReminderEntity(reportId = reportId, triggerAt = triggerAt, message = message))
        }
    }

    fun cancelReminder(id: Long) {
        viewModelScope.launch { repo.cancelReminder(id) }
    }
}