package com.example.medicord.Data.repository

import com.example.medicord.Data.database.dao.HistoriVisitDao
import com.example.medicord.Data.database.entity.HistoriVisitEntity
import kotlinx.coroutines.flow.Flow

class HistoriVisitRepository(private val historiVisitDao: HistoriVisitDao) {
    fun getHistoriByPasienId(pasienId: Long): Flow<List<HistoriVisitEntity>> =
        historiVisitDao.getHistoriByPasienId(pasienId)

    suspend fun getHistoriById(id: Long): HistoriVisitEntity? =
        historiVisitDao.getHistoriById(id)

    suspend fun insertHistori(histori: HistoriVisitEntity): Long =
        historiVisitDao.insertHistori(histori)

    suspend fun updateHistori(histori: HistoriVisitEntity) =
        historiVisitDao.updateHistori(histori)

    suspend fun deleteHistori(histori: HistoriVisitEntity) =
        historiVisitDao.deleteHistori(histori)

    suspend fun deleteHistoriByPasienId(pasienId: Long) =
        historiVisitDao.deleteHistoriByPasienId(pasienId)
}