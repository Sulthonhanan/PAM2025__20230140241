package com.example.medicord.Data.repository

import com.example.medicord.Data.database.dao.ObatDao
import com.example.medicord.Data.database.entity.ObatEntity
import kotlinx.coroutines.flow.Flow

class ObatRepository(private val obatDao: ObatDao) {
    fun getObatByPasienId(pasienId: Long): Flow<List<ObatEntity>> =
        obatDao.getObatByPasienId(pasienId)

    suspend fun getObatById(id: Long): ObatEntity? = obatDao.getObatById(id)

    suspend fun insertObat(obat: ObatEntity): Long = obatDao.insertObat(obat)

    suspend fun updateObat(obat: ObatEntity) = obatDao.updateObat(obat)

    suspend fun deleteObat(obat: ObatEntity) = obatDao.deleteObat(obat)

    suspend fun deleteObatByPasienId(pasienId: Long) = obatDao.deleteObatByPasienId(pasienId)
}