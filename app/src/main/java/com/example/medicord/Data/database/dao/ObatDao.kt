package com.example.medicord.Data.database.dao

import androidx.room.*
import com.example.medicord.Data.database.entity.ObatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ObatDao {
    @Query("SELECT * FROM obat WHERE pasienId = :pasienId ORDER BY id DESC")
    fun getObatByPasienId(pasienId: Long): Flow<List<ObatEntity>>

    @Query("SELECT * FROM obat WHERE id = :id")
    suspend fun getObatById(id: Long): ObatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObat(obat: ObatEntity): Long

    @Update
    suspend fun updateObat(obat: ObatEntity)

    @Delete
    suspend fun deleteObat(obat: ObatEntity)

    @Query("DELETE FROM obat WHERE pasienId = :pasienId")
    suspend fun deleteObatByPasienId(pasienId: Long)
}