package com.example.medicord.Data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pasien")
data class PasienEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val namaLengkap: String,
    val nik: String,
    val usia: Int,
    val jenisKelamin: String, // "Laki-laki" or "Perempuan"
    val alamat: String,
    val nomorTelepon: String,
    val catatanRiwayat: String?,
    val fotoPath: String? = null
)