package com.example.romero.proyectofinalappsmoviles.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.romero.proyectofinalappsmoviles.data.local.entity.SyncEventEntity

@Dao
interface SyncEventDao {

    @Query("SELECT * FROM sync_events WHERE synced = 0 ORDER BY pendingAt ASC")
    fun getPendingEvents(): LiveData<List<SyncEventEntity>>

    @Query("SELECT * FROM sync_events WHERE synced = 0")
    suspend fun getPendingEventsList(): List<SyncEventEntity>

    @Query("SELECT COUNT(*) FROM sync_events WHERE synced = 0")
    fun getPendingCount(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: SyncEventEntity): Long

    @Query("UPDATE sync_events SET synced = 1 WHERE id = :id")
    suspend fun markSynced(id: Long)

    @Query("DELETE FROM sync_events WHERE synced = 1")
    suspend fun clearSynced()
}