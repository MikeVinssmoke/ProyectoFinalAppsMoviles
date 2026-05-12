package com.example.romero.proyectofinalappsmoviles.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.romero.proyectofinalappsmoviles.data.local.AppDatabase
import com.example.romero.proyectofinalappsmoviles.data.local.entity.ReportEntity
import com.example.romero.proyectofinalappsmoviles.repository.ReportRepository
import kotlinx.coroutines.launch

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ReportRepository(AppDatabase.getInstance(application))

    val allReports   = repo.allReports
    val totalCount   = repo.totalCount
    val pendingCount = repo.pendingCount
    val syncedCount  = repo.syncedCount

    private val _statusFilter = MutableLiveData<String?>(null)
    val filteredReports: LiveData<List<ReportEntity>> = _statusFilter.switchMap { filter ->
        if (filter == null) repo.allReports else repo.getReportsByStatus(filter)
    }

    private val _currentReport = MutableLiveData<ReportEntity?>()
    val currentReport: LiveData<ReportEntity?> = _currentReport

    private val _operationResult = MutableLiveData<Result<Unit>?>()
    val operationResult: LiveData<Result<Unit>?> = _operationResult

    fun setStatusFilter(status: String?) { _statusFilter.value = status }

    fun loadReport(id: Long) {
        viewModelScope.launch { _currentReport.value = repo.getReportById(id) }
    }

    fun createReport(report: ReportEntity) {
        viewModelScope.launch {
            _operationResult.value = try {
                repo.createReport(report); Result.success(Unit)
            } catch (e: Exception) { Result.failure(e) }
        }
    }

    fun updateReport(report: ReportEntity) {
        viewModelScope.launch {
            _operationResult.value = try {
                repo.updateReport(report); Result.success(Unit)
            } catch (e: Exception) { Result.failure(e) }
        }
    }

    fun deleteReport(report: ReportEntity) {
        viewModelScope.launch { repo.deleteReport(report) }
    }

    fun clearResult() { _operationResult.value = null }
}