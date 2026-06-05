package com.example.romero.proyectofinalappsmoviles.viewmodel

import android.app.Application
import android.util.Log
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
            Log.d("SYNC", "=== SYNC INICIADO ===")

            Log.d("SYNC", "Subiendo cambios locales...")
            val syncResult = reportRepo.syncWithServer()
            Log.d("SYNC", "syncResult isSuccess=${syncResult.isSuccess}")

            Log.d("SYNC", "Bajando datos del servidor...")
            val fetchResult = reportRepo.fetchFromServer()
            Log.d("SYNC", "fetchResult isSuccess=${fetchResult.isSuccess}")

            syncRepo.clearSynced()

            _syncResult.value = if (fetchResult.isFailure || syncResult.isFailure) {
                Result.failure(
                    syncResult.exceptionOrNull()
                        ?: fetchResult.exceptionOrNull()
                        ?: Exception("Error desconocido")
                )
            } else {
                Result.success(Unit)
            }

            _isSyncing.value = false
            Log.d("SYNC", "=== SYNC TERMINADO ===")
        }
    }
}