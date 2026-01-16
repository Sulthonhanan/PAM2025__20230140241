package com.example.medicord.Data.model

data class Obat(
    val id: Long = 0,
    val pasienId: Long,
    val namaObat: String,
    val dosis: String,
    val frekuensi: String,
    val catatan: String?
)