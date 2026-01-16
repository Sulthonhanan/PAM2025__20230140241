package com.example.medicord.Data.model

import java.util.Date

data class HistoriVisit(
    val id: Long = 0,
    val pasienId: Long,
    val tanggalKunjungan: Date,
    val catatan: String,
    val keluhan: String?
)