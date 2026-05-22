package com.example.romero.proyectofinalappsmoviles.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.romero.proyectofinalappsmoviles.data.local.entity.ReportEntity

@Dao
interface ReportDao {

    @Query("SELECT * FROM reports ORDER BY date DESC")
    fun getAllReports(): LiveData<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE status = :status ORDER BY date DESC")
    fun getReportsByStatus(status: String): LiveData<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getReportById(id: Long): ReportEntity?

    @Query("SELECT * FROM reports WHERE isSynced = 0")
    suspend fun getUnsyncedReports(): List<ReportEntity>

    @Query("SELECT COUNT(*) FROM reports")
    fun getTotalCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM reports WHERE status != 'CERRADO'")
    fun getPendingCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM reports WHERE isSynced = 1")
    fun getSyncedCount(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: ReportEntity): Long

    @Update
    suspend fun update(report: ReportEntity)

    @Delete
    suspend fun delete(report: ReportEntity)

    @Query("UPDATE reports SET isSynced = 1, serverId = :serverId WHERE id = :id")
    suspend fun markSynced(id: Long, serverId: Long)

    @Query("SELECT * FROM reports WHERE serverId = :serverId LIMIT 1")
    suspend fun getReportByServerId(serverId: Long): ReportEntity?
}