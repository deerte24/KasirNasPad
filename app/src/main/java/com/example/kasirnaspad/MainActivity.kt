package com.example.kasirnaspad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasirnaspad.data.AppDatabase
import com.example.kasirnaspad.data.KasirRepository
import com.example.kasirnaspad.data.KasirViewModel
import com.example.kasirnaspad.data.KasirViewModelFactory
import com.example.kasirnaspad.data.Menu
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
                    MainScreen(viewModel = viewModel())
                }
            }
        }
    }
}

@Composable
fun MenuScreen(viewModel: KasirViewModel){
    val daftarMenu by viewModel.semuaMenu.collectAsState(initial = emptyList())

    var namaMenu by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()){
        Text(
            text = "Tambah Menu",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = namaMenu,
            onValueChange = { namaMenu = it},
            label = { Text("Nama Menu")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = harga,
            onValueChange = { harga = it},
            label = { Text("Harga jual")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = kategori,
            onValueChange = { kategori = it},
            label = { Text("Kategori (makanan/minuman")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = stok,
            onValueChange = { stok = it},
            label = { Text("Stok hari ini")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val hargaDouble = harga.toDoubleOrNull()
                val stokInt = stok.toIntOrNull()

                if(namaMenu.isNotBlank() && hargaDouble != null && kategori.isNotBlank() && stokInt != null){
                    viewModel.tambahMenu(
                        Menu(
                            namaMenu = namaMenu,
                            harga = hargaDouble,
                            kategori = kategori,
                            stokHariIni = stokInt
                        )
                    )
                    namaMenu = ""
                    harga = ""
                    kategori = ""
                    stok = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Menu")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Daftar Menu",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (daftarMenu.isEmpty()) {
            Text("Belum ada menu")
        } else {
            LazyColumn {
                items(daftarMenu) { menu ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
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

@Composable
fun MainScreen(viewModel: KasirViewModel){
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar() {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0},
                    icon = {},
                    label = { Text("Menu")}
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1},
                    icon = {},
                    label = { Text("Kasir")}
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            when (selectedTab){
                0 -> MenuScreen(viewModel = viewModel)
                1 -> KasirScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun KasirScreen(viewModel: KasirViewModel){
    val daftarMenu by viewModel.semuaMenu.collectAsState(initial = emptyList())

    val keranjang = remember { mutableStateMapOf<Int, Int>() }
    val metodePembayaran by remember { mutableStateOf("CASH") }

    val totalHarga = daftarMenu.sumOf { menu ->
        val jumlah = keranjang[menu.id] ?: 0
        menu.harga * jumlah
    }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Text("Pilih Menu", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(daftarMenu){ menu ->
                val jumlahDipilih = keranjang[menu.id] ?: 0

                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(menu.namaMenu, style = MaterialTheme.typography.titleMedium)
                            Text("Rp${menu.harga.toInt()} | Stok: ${menu.stokHariIni}")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                if (jumlahDipilih > 0){
                                    keranjang[menu.id] = jumlahDipilih - 1
                                }
                            }) { Text("-")}

                            Text("$jumlahDipilih", modifier = Modifier.padding(horizontal = 8.dp))

                            IconButton(onClick = {
                                if (jumlahDipilih < menu.stokHariIni){
                                    keranjang[menu.id] = jumlahDipilih + 1
                                }
                            }) { Text("+")}
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Total: Rp${totalHarga.toInt()}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Metode Pembayaran")
        Row {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = metodePembayaran == "CASH",
                    onClick = { metodePembayaran == "CASH"}
                )
                Text("Cash")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = metodePembayaran == "QRIS",
                    onClick = { metodePembayaran == "QRIS"}
                )
                Text("QRIS")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                val itemKeranjang = daftarMenu.mapNotNull { menu->
                    val jumlah = keranjang[menu.id] ?: 0
                    if (jumlah > 0) menu to jumlah else null
                }
                if (itemKeranjang.isNotEmpty()){
                    viewModel.checkout(itemKeranjang, metodePembayaran)
                    keranjang.clear()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = totalHarga > 0
        ) { Text("Checkout")}
    }
}