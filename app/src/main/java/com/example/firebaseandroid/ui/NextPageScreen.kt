package com.example.firebaseandroid.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.firebaseandroid.viewmodels.NextPageViewModel

@Composable
fun NextPageScreen(viewModel: NextPageViewModel) {
    val quotes by viewModel.quotes.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchQuotes()  // ✅ Fetch quotes on screen load
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Next Page") })
        },
        content = {
            LazyColumn {
                items(quotes) { quote ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = quote.title, fontWeight = FontWeight.Bold)
                            Text(text = quote.body)
                        }
                    }
                }
            }
        }
    )
}