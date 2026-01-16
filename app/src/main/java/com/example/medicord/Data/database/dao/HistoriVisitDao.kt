package com.example.medicord.Data.database.dao

import androidx.room.*
import com.example.medicord.Data.database.entity.HistoriVisitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoriVisitDao {
    @Query("SELECT * FROM histori_visit WHERE pasienId = :pasienId ORDER BY tanggalKunjungan DESC")
    fun getHistoriByPasienId(pasienId: Long): Flow<List<HistoriVisitEntity>>

    @Query("SELECT * FROM histori_visit WHERE id = :id")
    suspend fun getHistoriById(id: Long): HistoriVisitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistori(histori: HistoriVisitEntity): Long

    @Update
    suspend fun updateHistori(histori: HistoriVisitEntity)

    @Delete
    suspend fun deleteHistori(histori: HistoriVisitEntity)

    @Query("DELETE FROM histori_visit WHERE pasienId = :pasienId")
    suspend fun deleteHistoriByPasienId(pasienId: Long)
}