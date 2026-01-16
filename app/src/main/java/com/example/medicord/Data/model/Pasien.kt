package com.example.medicord.Data.model

data class Pasien(
    val id: Long = 0,
    val namaLengkap: String,
    val nik: String,
    val usia: Int,
    val jenisKelamin: String,
    val alamat: String,
    val nomorTelepon: String,
    val catatanRiwayat: String?,
    val fotoPath: String? = null
)