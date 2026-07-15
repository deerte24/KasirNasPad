package com.example.kasirnaspad.data

import androidx.room.*

@Dao
interface DetailTransaksiDao{
    @Insert
    suspend fun insertDetailTransaksi(detail: DetailTransaksi)

    @Insert
    suspend fun insertAllDetailtransaksi(detailList: List<DetailTransaksi>)

    @Query("SELECT * FROM detail_transaksi WHERE idTransaksi = :transaksiId")
    suspend fun getDetailByTransaksiId(transaksiId: Int): List<DetailTransaksi>
}