package com.example.medicord.Data.database.dao

import androidx.room.*
import com.example.medicord.Data.database.entity.PasienEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PasienDao {
    @Query("SELECT * FROM pasien ORDER BY namaLengkap ASC")
    fun getAllPasien(): Flow<List<PasienEntity>>

    @Query("SELECT * FROM pasien WHERE id = :id")
    suspend fun getPasienById(id: Long): PasienEntity?

    @Query("SELECT * FROM pasien WHERE namaLengkap LIKE '%' || :query || '%' OR nik LIKE '%' || :query || '%'")
    fun searchPasien(query: String): Flow<List<PasienEntity>>

    @Query("SELECT * FROM pasien WHERE jenisKelamin = :jenisKelamin")
    fun filterByJenisKelamin(jenisKelamin: String): Flow<List<PasienEntity>>

    @Query("SELECT * FROM pasien WHERE usia BETWEEN :minAge AND :maxAge")
    fun filterByUsia(minAge: Int, maxAge: Int): Flow<List<PasienEntity>>

    @Query("SELECT * FROM pasien WHERE (namaLengkap LIKE '%' || :query || '%' OR nik LIKE '%' || :query || '%') AND jenisKelamin = :jenisKelamin")
    fun searchAndFilterByJenisKelamin(query: String, jenisKelamin: String): Flow<List<PasienEntity>>

    @Query("SELECT * FROM pasien WHERE (namaLengkap LIKE '%' || :query || '%' OR nik LIKE '%' || :query || '%') AND usia BETWEEN :minAge AND :maxAge")
    fun searchAndFilterByUsia(query: String, minAge: Int, maxAge: Int): Flow<List<PasienEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasien(pasien: PasienEntity): Long

    @Update
    suspend fun updatePasien(pasien: PasienEntity)

    @Delete
    suspend fun deletePasien(pasien: PasienEntity)

    @Query("DELETE FROM pasien WHERE id = :id")
    suspend fun deletePasienById(id: Long)
}