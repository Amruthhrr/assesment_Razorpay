package com.example.firebaseandroid.ui.screens.wallscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.firebaseandroid.models.Tweet
import com.example.firebaseandroid.models.TweetType

@Composable
fun TweetCard(
    tweet: Tweet,
    onEdit: (Tweet) -> Unit,
    onDelete: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tweet.userName,
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                if (tweet.type == TweetType.TEXT) {
                    Text(
                        text = tweet.message,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                } else if (tweet.type == TweetType.IMAGE) {
                    val imageLoader = rememberImagePainter(
                        data = tweet.message,
                        builder = { crossfade(true) }
                    )
                    Image(
                        painter = imageLoader,
                        contentDescription = "Tweet Image",
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .size(350.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }

            // Edit and Delete Buttons
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                IconButton(onClick = { onEdit(tweet) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Tweet", tint = MaterialTheme.colors.primary)
                }
                IconButton(onClick = { onDelete(tweet.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Tweet", tint = MaterialTheme.colors.error)
                }
            }
        }
    }
}