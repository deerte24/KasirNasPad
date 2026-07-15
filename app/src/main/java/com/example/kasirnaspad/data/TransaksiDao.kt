package com.example.kasirnaspad.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaksiDao{
    @Insert
    suspend fun insertTransaksi(transaksi: Transaksi): Long

    @Query("SELECT * FROM transaksi ORDER BY tanggalwaktu DESC")
    fun getAllTransaksi(): Flow<List<Transaksi>>

    @Query("SELECT * FROM transaksi WHERE metodePembayaran = :metode ORDER BY tanggalwaktu DESC")
    fun getTransaksiByMetode(metode: String): Flow<List<Transaksi>>

    @Query("SELECT SUM(totalHarga) FROM transaksi WHERE metodePembayaran = :metode")
    fun getTotalByMetode(metode: String): Flow<Double?>
}