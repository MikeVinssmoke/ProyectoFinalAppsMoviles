package com.example.romero.proyectofinalappsmoviles.repository

import android.util.Log
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
        android.util.Log.d("EDIT", "updateReport llamado id=${report.id} title=${report.title}")
        db.reportDao().update(report.copy(isSynced = false))
        android.util.Log.d("EDIT", "Room actualizado OK")
        db.syncEventDao().insert(SyncEventEntity(reportId = report.id, action = "UPDATE"))
    }

    suspend fun deleteReport(report: ReportEntity) = db.reportDao().delete(report)

    suspend fun syncWithServer(): Result<Unit> = try {
        val pending = db.reportDao().getUnsyncedReports()
        Log.d("SYNC", "Reportes pendientes de subir: ${pending.size}")

        for (r in pending) {
            Log.d("SYNC", "Subiendo reporte id=${r.id} title=${r.title} serverId=${r.serverId}")
            val req = ReportRequest(
                r.title, r.description, r.category,
                r.priority, r.status, r.location
            )
            if (r.serverId == null) {
                val resp = NetworkManager.apiService.createReport(req)
                Log.d("SYNC", "POST response: ${resp.code()} isSuccessful=${resp.isSuccessful}")
                if (resp.isSuccessful) resp.body()?.let {
                    Log.d("SYNC", "Creado en servidor con id=${it.id}")
                    db.reportDao().markSynced(r.id, it.id)
                    val events = db.syncEventDao().getPendingEventsList()
                    events.filter { e -> e.reportId == r.id }.forEach { e ->
                        db.syncEventDao().markSynced(e.id)
                    }
                }
            } else {
                val resp = NetworkManager.apiService.updateReport(r.serverId, req)
                Log.d("SYNC", "PUT response: ${resp.code()} serverId=${r.serverId}")
                db.reportDao().markSynced(r.id, r.serverId)
                val events = db.syncEventDao().getPendingEventsList()
                events.filter { e -> e.reportId == r.id }.forEach { e ->
                    db.syncEventDao().markSynced(e.id)
                }
            }
        }
        db.syncEventDao().clearSynced()
        Log.d("SYNC", "syncWithServer completado OK")
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("SYNC", "syncWithServer ERROR: ${e.message}", e)
        Result.failure(e)
    }

    suspend fun fetchFromServer(): Result<Unit> {
        return try {
            Log.d("SYNC", "fetchFromServer iniciando...")
            val response = NetworkManager.apiService.getReports()
            Log.d("SYNC", "fetchFromServer response: ${response.code()} isSuccessful=${response.isSuccessful}")

            if (response.isSuccessful) {
                response.body()?.let { remoteReports ->
                    Log.d("SYNC", "Reportes del servidor: ${remoteReports.size}")
                    for (r in remoteReports) {
                        val existing = db.reportDao().getReportByServerId(r.id)
                        if (existing == null) {
                            Log.d("SYNC", "Insertando nuevo reporte serverId=${r.id}")
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
                        } else {
                            Log.d("SYNC", "Actualizando reporte existente serverId=${r.id}")
                            db.reportDao().update(
                                existing.copy(
                                    title       = r.title,
                                    description = r.description,
                                    category    = r.category,
                                    priority    = r.priority,
                                    status      = r.status,
                                    location    = r.location,
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
            Log.e("SYNC", "fetchFromServer ERROR: ${e.message}", e)
            Result.failure(e)
        }
    }
}