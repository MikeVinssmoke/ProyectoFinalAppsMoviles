package com.example.romero.proyectofinalappsmoviles.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.romero.proyectofinalappsmoviles.data.local.dao.*
import com.example.romero.proyectofinalappsmoviles.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ReportEntity::class,
        CategoryEntity::class,
        ReminderEntity::class,
        SyncEventEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reportDao(): ReportDao
    abstract fun categoryDao(): CategoryDao
    abstract fun reminderDao(): ReminderDao
    abstract fun syncEventDao(): SyncEventDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nodocivico_db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                database.categoryDao().insertAll(listOf(
                                    CategoryEntity(name = "Alumbrado",        iconKey = "alumbrado"),
                                    CategoryEntity(name = "Aseo",             iconKey = "aseo"),
                                    CategoryEntity(name = "Seguridad",        iconKey = "seguridad"),
                                    CategoryEntity(name = "Servicios públicos", iconKey = "servicios"),
                                    CategoryEntity(name = "Vías",             iconKey = "vias"),
                                    CategoryEntity(name = "Otro",             iconKey = "otro")
                                ))
                            }
                        }
                    }
                }).build().also { INSTANCE = it }
            }
        }
    }
}