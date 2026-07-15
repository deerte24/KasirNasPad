package com.example.kasirnaspad.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "detail_transaksi",
    foreignKeys = [
        ForeignKey(
            entity = Transaksi::class,
            parentColumns = ["id"],
            childColumns = ["idTransaksi"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Menu::class,
            parentColumns = ["id"],
            childColumns = ["idMenu"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DetailTransaksi(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idTransaksi: Int,
    val idMenu: Int,
    val jumlah: Int,
    val subtotal: Double
)