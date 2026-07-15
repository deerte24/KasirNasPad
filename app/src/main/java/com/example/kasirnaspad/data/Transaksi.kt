package com.example.kasirnaspad.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi")
data class Transaksi(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val TanggalWaktu: Long,
    val TotalHarga: Double,
    val MetodePembayaran: String
)