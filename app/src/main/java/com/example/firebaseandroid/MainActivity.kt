package com.example.firebaseandroid

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebaseandroid.ui.screens.AuthScreen
import com.example.firebaseandroid.ui.screens.WallScreen
import com.example.firebaseandroid.viewmodels.log.FirebaseLogViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseandroid.ui.NextPageScreen

import com.example.firebaseandroid.amruth.retrofit.QuotesRepository
import com.example.firebaseandroid.amruth.retrofit.RetrofitInstance
import com.example.firebaseandroid.viewmodels.NextPageViewModel
import com.example.firebaseandroid.viewmodels.NextPageViewModelFactory
import com.example.firebaseandroid.viewmodels.auth.FirebaseAuthViewModel
import com.example.firebaseandroid.viewmodels.storage.FirebaseStorageViewModel
import com.example.firebaseandroid.viewmodels.tweets.FirestoreTweetsViewModel
import com.google.firebase.messaging.ktx.messaging

class MainActivity : ComponentActivity() {
    private lateinit var  firebaseAnalytics: FirebaseAnalytics

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Firebase.messaging.subscribeToTopic("all")
                .addOnCompleteListener { task ->
                    var msg = "Subscribed"
                    if (!task.isSuccessful) {
                        msg = "Subscribe failed"
                    }
                    Log.d("FCM", msg)
                }
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }




    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        askNotificationPermission()
        setContent {
            val navController = rememberNavController()
            val authViewModel = FirebaseAuthViewModel()
            val tweetsViewModel = FirestoreTweetsViewModel()
            val storageViewModel = FirebaseStorageViewModel()
            val logViewModel = FirebaseLogViewModel()

            val apiService = RetrofitInstance.api
            val quotesRepository = QuotesRepository(apiService)
            val viewModelFactory = NextPageViewModelFactory(quotesRepository)
            val nextPageViewModel: NextPageViewModel = ViewModelProvider(this, viewModelFactory)[NextPageViewModel::class.java]

            NavHost(navController = navController, startDestination = Routes.AUTH_SCREEN) {
                composable(Routes.AUTH_SCREEN) {
                    AuthScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        logViewModel = logViewModel
                    )
                }
                composable(Routes.WALL_SCREEN) {
                    WallScreen(
                        tweetsViewModel = tweetsViewModel,
                        authViewModel = authViewModel,
                        navController = navController,
                        storageViewModel = storageViewModel,
                        logViewModel = logViewModel
                    )
                }
                // Ensure NextPageViewModel is properly passed
                composable("next_page") {
                    NextPageScreen(viewModel = nextPageViewModel)
                }
            }
        }
    }
}
