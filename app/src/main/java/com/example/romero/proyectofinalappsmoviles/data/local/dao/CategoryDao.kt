package com.example.romero.proyectofinalappsmoviles.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.romero.proyectofinalappsmoviles.data.local.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun count(): Int
}