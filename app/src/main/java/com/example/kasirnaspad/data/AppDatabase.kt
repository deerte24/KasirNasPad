package com.example.kasirnaspad.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Menu::class, Transaksi::class, DetailTransaksi::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase(){
    abstract fun menuDao() : menuDao
    abstract fun transaksiDao(): TransaksiDao
    abstract fun detailTransaksiDao(): DetailTransaksiDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kasir_padang_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}