package com.example.medicord.Data.repository

import com.example.medicord.Data.database.dao.PasienDao
import com.example.medicord.Data.database.entity.PasienEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PasienRepository(private val pasienDao: PasienDao) {
    fun getAllPasien(): Flow<List<PasienEntity>> = pasienDao.getAllPasien()

    suspend fun getPasienById(id: Long): PasienEntity? = pasienDao.getPasienById(id)

    fun searchPasien(query: String): Flow<List<PasienEntity>> = pasienDao.searchPasien(query)

    fun filterByJenisKelamin(jenisKelamin: String): Flow<List<PasienEntity>> =
        pasienDao.filterByJenisKelamin(jenisKelamin)

    fun filterByUsia(minAge: Int, maxAge: Int): Flow<List<PasienEntity>> =
        pasienDao.filterByUsia(minAge, maxAge)

    fun searchAndFilterByJenisKelamin(query: String, jenisKelamin: String): Flow<List<PasienEntity>> =
        pasienDao.searchAndFilterByJenisKelamin(query, jenisKelamin)

    fun searchAndFilterByUsia(query: String, minAge: Int, maxAge: Int): Flow<List<PasienEntity>> =
        pasienDao.searchAndFilterByUsia(query, minAge, maxAge)

    suspend fun insertPasien(pasien: PasienEntity): Long = pasienDao.insertPasien(pasien)

    suspend fun updatePasien(pasien: PasienEntity) = pasienDao.updatePasien(pasien)

    suspend fun deletePasien(pasien: PasienEntity) = pasienDao.deletePasien(pasien)

    suspend fun deletePasienById(id: Long) = pasienDao.deletePasienById(id)
}