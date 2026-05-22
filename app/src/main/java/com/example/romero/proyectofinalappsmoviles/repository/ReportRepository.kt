package com.example.romero.proyectofinalappsmoviles.repository

import com.example.romero.proyectofinalappsmoviles.data.local.AppDatabase
import com.example.romero.proyectofinalappsmoviles.data.local.entity.ReportEntity
import com.example.romero.proyectofinalappsmoviles.data.local.entity.SyncEventEntity
import com.example.romero.proyectofinalappsmoviles.data.remote.NetworkManager
import com.example.romero.proyectofinalappsmoviles.data.remote.model.ReportRequest

class ReportRepository(private val db: AppDatabase) {

    val allReports   = db.reportDao().getAllReports()
    val totalCount   = db.reportDao().getTotalCount()
    val pendingCount = db.reportDao().getPendingCount()
    val syncedCount  = db.reportDao().getSyncedCount()

    fun getReportsByStatus(status: String) = db.reportDao().getReportsByStatus(status)
    suspend fun getReportById(id: Long)    = db.reportDao().getReportById(id)

    suspend fun createReport(report: ReportEntity): Long {
        val id = db.reportDao().insert(report)
        db.syncEventDao().insert(SyncEventEntity(reportId = id, action = "CREATE"))
        return id
    }

    suspend fun updateReport(report: ReportEntity) {
        db.reportDao().update(report)
        db.syncEventDao().insert(SyncEventEntity(reportId = report.id, action = "UPDATE"))
    }

    suspend fun deleteReport(report: ReportEntity) = db.reportDao().delete(report)

    suspend fun syncWithServer(): Result<Unit> = try {
        val pending = db.reportDao().getUnsyncedReports()
        for (r in pending) {
            val req = ReportRequest(r.title, r.description, r.category, r.priority, r.status, r.location)
            if (r.serverId == null) {
                val resp = NetworkManager.apiService.createReport(req)
                if (resp.isSuccessful) resp.body()?.let { db.reportDao().markSynced(r.id, it.id) }
            } else {
                NetworkManager.apiService.updateReport(r.serverId, req)
                db.reportDao().markSynced(r.id, r.serverId)
            }
        }
        db.syncEventDao().clearSynced()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }


    suspend fun fetchFromServer(): Result<Unit> {
        return try {
            val response = NetworkManager.apiService.getReports()
            if (response.isSuccessful) {
                response.body()?.let { remoteReports ->
                    for (r in remoteReports) {
                        val existing = db.reportDao().getReportByServerId(r.id)
                        if (existing == null) {
                            db.reportDao().insert(
                                ReportEntity(
                                    title       = r.title,
                                    description = r.description,
                                    category    = r.category,
                                    priority    = r.priority,
                                    status      = r.status,
                                    location    = r.location,
                                    date        = r.date,
                                    isSynced    = true,
                                    serverId    = r.id
                                )
                            )
                        }
                    }
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}