package com.example.firebaseandroid.amruth.retrofit

import com.example.firebaseandroid.models.QuoteItem


class QuotesRepository(private val apiService: ApiService) {

    // Fetch quotes from the API
    suspend fun getQuotes(): List<QuoteItem> {
        return try {
            apiService.getQuotes() // Retrofit API Call
        } catch (e: Exception) {
            emptyList() // Return an empty list if API call fails
        }
    }
}