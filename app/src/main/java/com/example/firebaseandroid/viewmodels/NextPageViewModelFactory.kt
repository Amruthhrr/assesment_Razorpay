package com.example.firebaseandroid.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseandroid.amruth.retrofit.QuotesRepository

class NextPageViewModelFactory(private val quotesRepository: QuotesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NextPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NextPageViewModel(quotesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}