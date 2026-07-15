package com.example.kasirnaspad.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface menuDao {

    @Insert
    suspend fun insertMenu(menu: Menu)

    @Update
    suspend fun updateMenu(menu: Menu)

    @Delete
    suspend fun deleteMenu(menu: Menu)

    @Query("SELECT * FROM menu")
    fun getAllMenu(): Flow<List<Menu>>

    @Query("UPDATE menu SET stokHariIni = stokHariIni - :jumlah WHERE id = :menuId")
    suspend fun kurangiStok(menuId: Int, jumlah: Int)

}