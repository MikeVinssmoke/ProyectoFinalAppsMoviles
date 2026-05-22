package com.example.romero.proyectofinalappsmoviles.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.romero.proyectofinalappsmoviles.data.local.AppDatabase
import com.example.romero.proyectofinalappsmoviles.repository.ReportRepository
import com.example.romero.proyectofinalappsmoviles.repository.SyncEventRepository
import com.example.romero.proyectofinalappsmoviles.util.NetworkUtils
import kotlinx.coroutines.launch

class SyncViewModel(application: Application) : AndroidViewModel(application) {

    private val reportRepo = ReportRepository(AppDatabase.getInstance(application))
    private val syncRepo   = SyncEventRepository(AppDatabase.getInstance(application))

    val pendingEvents = syncRepo.pendingEvents
    val pendingCount  = syncRepo.pendingCount

    private val _isOnline    = MutableLiveData<Boolean>()
    val isOnline: LiveData<Boolean> = _isOnline

    private val _isSyncing   = MutableLiveData(false)
    val isSyncing: LiveData<Boolean> = _isSyncing

    private val _syncResult  = MutableLiveData<Result<Unit>?>()
    val syncResult: LiveData<Result<Unit>?> = _syncResult

    fun checkConnectivity() {
        _isOnline.value = NetworkUtils.isOnline(getApplication())
    }

    fun synchronize() {
        viewModelScope.launch {
            _isSyncing.value = true
            reportRepo.fetchFromServer()  // ← primero descarga
            _syncResult.value = reportRepo.syncWithServer()  // luego sube
            _isSyncing.value = false
        }
    }
}