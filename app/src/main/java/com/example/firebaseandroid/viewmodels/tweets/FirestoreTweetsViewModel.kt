package com.example.firebaseandroid.viewmodels.tweets

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.firebaseandroid.models.Tweet
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirestoreTweetsViewModel : TweetsViewModelInterface, ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val tweetsCollection = db.collection("tweets")
    private val analytics: FirebaseAnalytics = Firebase.analytics  // Firebase Analytics Instance
    private val TAG = "FirestoreTweetsVM"

    override fun fetchTweets(onSuccess: (List<Tweet>) -> Unit, onFailure: (Exception) -> Unit) {
        Log.d(TAG, "Fetching tweets from Firestore...")

        tweetsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    Log.e(TAG, "Error fetching tweets: ${exception.message}", exception)
                    onFailure(exception)
                    return@addSnapshotListener
                }

                val tweets = querySnapshot?.documents?.mapNotNull { document ->
                    document.toObject(Tweet::class.java)?.apply {
                        id = document.id  // Assign Firestore document ID
                    }
                } ?: emptyList()

                Log.d(TAG, "Fetched ${tweets.size} tweets successfully.")
                logFetchTweetsEvent(tweets.size)
                onSuccess(tweets)
            }
    }

    override fun postTweet(tweet: Tweet, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        Log.d(TAG, "Posting a new tweet: $tweet")

        tweetsCollection
            .add(tweet)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Tweet posted successfully!")
                logPostTweetEvent(documentReference.id, tweet.id, tweet.message)
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to post tweet: ${exception.message}", exception)
                onFailure(exception)
            }
    }

    override fun editTweet(tweetId: String, updatedTweet: Tweet, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        Log.d(TAG, "Editing tweet with ID: $tweetId")

        tweetsCollection.document(tweetId)
            .set(updatedTweet)
            .addOnSuccessListener {
                Log.d(TAG, "Tweet edited successfully!")
                logUpdateTweetEvent(tweetId, updatedTweet.id)
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to edit tweet: ${exception.message}", exception)
                onFailure(exception)
            }
    }

    override fun deleteTweet(tweetId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        Log.d(TAG, "Deleting tweet with ID: $tweetId")

        tweetsCollection.document(tweetId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Tweet deleted successfully!")
                logDeleteTweetEvent(tweetId)
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to delete tweet: ${exception.message}", exception)
                onFailure(exception)
            }
    }

    // Log event for fetching tweets
    private fun logFetchTweetsEvent(tweetCount: Int) {
        val params = Bundle().apply {
            putInt("tweet_count", tweetCount)
        }
        analytics.logEvent("fetch_tweets", params)
    }
    // Log event for posting a tweet
    private fun logPostTweetEvent(tweetId: String, userId: String?, text: String?) {
        val params = Bundle().apply {
            putString("tweet_id", tweetId)
            putString("user_id", userId ?: "unknown")
            putString("tweet_text", text?.take(100)) // Limit to 100 characters
        }
        analytics.logEvent("post_tweet", params)
    }

    // Log event for updating a tweet
    private fun logUpdateTweetEvent(tweetId: String, userId: String?) {
        val params = Bundle().apply {
            putString("tweet_id", tweetId)
            putString("user_id", userId ?: "unknown")
        }
        analytics.logEvent("update_tweet", params)
    }

    // Log event for deleting a tweet
    private fun logDeleteTweetEvent(tweetId: String) {
        val params = Bundle().apply {
            putString("tweet_id", tweetId)
        }
        analytics.logEvent("delete_tweet", params)
    }
}