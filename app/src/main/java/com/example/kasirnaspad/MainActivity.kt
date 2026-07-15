package com.example.kasirnaspad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasirnaspad.data.AppDatabase
import com.example.kasirnaspad.data.KasirRepository
import com.example.kasirnaspad.data.KasirViewModel
import com.example.kasirnaspad.data.KasirViewModelFactory
import com.example.kasirnaspad.data.menuDao
import com.example.kasirnaspad.ui.theme.KasirNasPadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)

        val repository = KasirRepository(
            menuDao = database.menuDao(),
            transaksiDao = database.transaksiDao(),
            detailTransaksiDao = database.detailTransaksiDao()
        )

        val factory = KasirViewModelFactory(repository)

        setContent {
            KasirNasPadTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    val ViewModel: KasirViewModel = viewModel(factory = factory)
                    MenuScreen(viewModel = viewModel())
                }
            }
        }
    }
}

@Composable
fun MenuScreen(viewModel: KasirViewModel){
    val daftarMenu by viewModel.semuaMenu.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)){
        Text(
            text = "Daftar Menu",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        if(daftarMenu.isEmpty()){
            Text("Belum ada menu")
        }else{
            LazyColumn{
                items(daftarMenu){ menu ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)){
                            Text(text = menu.namaMenu, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Rp${menu.harga.toInt()}")
                            Text(text = "Stok: ${menu.stokHariIni}")
                        }
                    }
                }
            }
        }
    }
}