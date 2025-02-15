package com.example.firebaseandroid.models

data class Tweet(
    var id: String = "",          // Firestore document ID (default empty)
    val userName: String = "",
    val type: TweetType = TweetType.TEXT,
    val message: String = "",
    val timestamp: String = ""
) {
    // No-arg constructor for Firestore
    constructor() : this("", "", TweetType.TEXT, "", "")
}