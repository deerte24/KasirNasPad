package com.example.kasirnaspad.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu")
data class Menu(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaMenu: String,
    val harga: Double,
    val kategori: String,
    val stokHariIni: Int
)