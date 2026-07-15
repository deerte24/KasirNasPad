package com.example.kasirnaspad.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class KasirViewModel(private val repository: KasirRepository) : ViewModel(){
    val semuaMenu = repository.semuaMenu
    val semuaTransaksi = repository.semuaTransaksi

    fun tambahMenu(menu: Menu){
        viewModelScope.launch {
            repository.tambahMenu(menu)
        }
    }

    fun checkout(itemKeranjang: List<Pair<Menu, Int>>, metodePembayaran: String){
        viewModelScope.launch {
            repository.prosesCheckout(itemKeranjang, metodePembayaran)
        }
    }

    fun getTransaksiByMetode(metode: String) = repository.getTransaksiByMetode(metode)
    fun getTotalByMetode(metode: String) = repository.getTotalByMetode(metode)
}