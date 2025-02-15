package com.example.firebaseandroid.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseandroid.amruth.retrofit.QuotesRepository
import com.example.firebaseandroid.models.QuoteItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NextPageViewModel(private val quotesRepository: QuotesRepository) : ViewModel() {

    private val _quotes = MutableStateFlow<List<QuoteItem>>(emptyList())
    val quotes: StateFlow<List<QuoteItem>> = _quotes.asStateFlow()

    fun fetchQuotes() {
        viewModelScope.launch {
            try {
                val response = quotesRepository.getQuotes() // ✅ Fetch from repository
                _quotes.value = response
            } catch (e: Exception) {
                Log.e("NextPageViewModel", "Error fetching quotes", e)
            }
        }
    }




}