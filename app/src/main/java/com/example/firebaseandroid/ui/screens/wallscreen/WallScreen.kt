package com.example.firebaseandroid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.firebaseandroid.Routes
import com.example.firebaseandroid.models.Tweet
import com.example.firebaseandroid.viewmodels.tweets.TweetsViewModelInterface
import com.example.firebaseandroid.viewmodels.storage.StorageViewModelInterface
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.unit.dp
import com.example.firebaseandroid.ui.screens.wallscreen.TweetForm
import com.example.firebaseandroid.ui.screens.wallscreen.TweetList
import com.example.firebaseandroid.viewmodels.auth.AuthViewModelInterface
import com.example.firebaseandroid.viewmodels.log.LogViewModelInterface


@Composable
fun WallScreen(
    tweetsViewModel: TweetsViewModelInterface,
    authViewModel: AuthViewModelInterface,
    storageViewModel: StorageViewModelInterface,
    navController: NavController,
    logViewModel: LogViewModelInterface
) {
    val screenName = "WallScreen"
    val (showErrorDialog, setShowErrorDialog) = remember { mutableStateOf(false) }
    val (errorMessage, setErrorMessage) = remember { mutableStateOf("") }
    val (tweets, setTweets) = remember { mutableStateOf(emptyList<Tweet>()) }
    val (tweetToEdit, setTweetToEdit) = remember { mutableStateOf<Tweet?>(null) }

    // Fetch Tweets
    LaunchedEffect(Unit) {
        tweetsViewModel.fetchTweets(
            onSuccess = { fetchedTweets -> setTweets(fetchedTweets) },
            onFailure = { exception ->
                setErrorMessage(exception.message ?: "Unknown error")
                setShowErrorDialog(true)
                logViewModel.crash(screenName, exception)
            }
        )
    }

    fun editTweet(tweet: Tweet) {
        setTweetToEdit(tweet) // Set tweet for editing
    }

    fun deleteTweet(tweetId: String) {
        tweetsViewModel.deleteTweet(tweetId,
            onSuccess = {
                setTweets(tweets.filter { it.id != tweetId }) // Update UI
            },
            onFailure = { exception ->
                setErrorMessage(exception.message ?: "Error deleting tweet")
                setShowErrorDialog(true)
                logViewModel.crash(screenName, exception)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") },
                actions = {
                    TextButton(onClick = {
                        authViewModel.logout(onSuccess = {
                            navController.navigate(Routes.AUTH_SCREEN)
                        }, onFailure = { exception ->
                            logViewModel.crash(screenName, exception)
                        })
                    }) {
                        Text(
                            text = "Logout",
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column {
                    TweetList(
                        tweets = tweets,
                        modifier = Modifier.weight(3f),
                        onEditTweet = ::editTweet,
                        onDeleteTweet = ::deleteTweet
                    )

                    TweetForm(
                        tweetsViewModel,
                        authViewModel,
                        storageViewModel,
                        logViewModel,
                        setErrorMessage,
                        setShowErrorDialog,
                        modifier = Modifier.weight(1f),
                        tweetToEdit = tweetToEdit,
                        onTweetUpdated = {
                            setTweetToEdit(null) // Reset after update
                        }
                    )

                    Button(
                        onClick = { navController.navigate("next_page") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Go to Next Page")
                    }
                }
            }
        }
    )

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { setShowErrorDialog(false) },
            title = { Text(text = "Error") },
            text = { Text(text = errorMessage) },
            confirmButton = {
                TextButton(onClick = { setShowErrorDialog(false) }) {
                    Text("OK")
                }
            }
        )
    }
}