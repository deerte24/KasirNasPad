package com.example.kasirnaspad.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class KasirViewModelFactory(private val repository: KasirRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KasirViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return KasirViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}