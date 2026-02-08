package com.gih.campusone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.gih.campusone.ui.navigation.AppNavigation
import com.gih.campusone.ui.screens.auth.AuthViewModel
import com.gih.campusone.ui.theme.CampusOneTheme

/**
 * Main Activity - Entry point of the app
 * Sets up Compose, Navigation, and AuthViewModel
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusOneTheme {
                CampusOneApp()
            }
        }
    }
}

/**
 * Main Composable for the app
 * Sets up navigation and authentication
 */
@Composable
fun CampusOneApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    // AppNavigation handles all screen navigation based on auth state
    AppNavigation(
        navController = navController,
        authViewModel = authViewModel
    )
}