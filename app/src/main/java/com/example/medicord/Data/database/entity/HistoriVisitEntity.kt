package com.example.medicord.Data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "histori_visit",
    foreignKeys = [
        ForeignKey(
            entity = PasienEntity::class,
            parentColumns = ["id"],
            childColumns = ["pasienId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HistoriVisitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pasienId: Long,
    val tanggalKunjungan: Long, // Timestamp
    val catatan: String,
    val keluhan: String?
)
