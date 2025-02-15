package com.example.firebaseandroid.amruth.retrofit


import com.example.firebaseandroid.models.QuoteItem
import retrofit2.http.GET

interface ApiService {
    @GET("posts")  // Replace with your actual endpoint
    suspend fun getQuotes(): List<QuoteItem>
}