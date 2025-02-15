package com.example.firebaseandroid.models
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteItem(
    @PrimaryKey val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)