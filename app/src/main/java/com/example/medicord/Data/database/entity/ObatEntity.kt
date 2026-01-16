package com.example.medicord.Data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "obat",
    foreignKeys = [
        ForeignKey(
            entity = PasienEntity::class,
            parentColumns = ["id"],
            childColumns = ["pasienId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ObatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pasienId: Long,
    val namaObat: String,
    val dosis: String,
    val frekuensi: String,
    val catatan: String?
)
