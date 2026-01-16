package com.example.medicord.Data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medicord.Data.database.dao.*
import com.example.medicord.Data.database.entity.*
import com.example.medicord.Data.database.entity.converters.DateConverter

@Database(
    entities = [
        PasienEntity::class,
        UserEntity::class,
        HistoriVisitEntity::class,
        ObatEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pasienDao(): PasienDao
    abstract fun userDao(): UserDao
    abstract fun historiVisitDao(): HistoriVisitDao
    abstract fun obatDao(): ObatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medicord_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}