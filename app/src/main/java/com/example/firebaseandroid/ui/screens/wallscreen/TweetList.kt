package com.example.firebaseandroid.ui.screens.wallscreen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.firebaseandroid.models.Tweet

@Composable
fun TweetList(
    tweets: List<Tweet>,
    modifier: Modifier = Modifier,
    onEditTweet: (Tweet) -> Unit,
    onDeleteTweet: (String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(tweets) { _, tweet ->
            TweetCard(
                tweet = tweet,
                onEdit = onEditTweet,
                onDelete = onDeleteTweet
            )
            Divider()
        }
    }
}