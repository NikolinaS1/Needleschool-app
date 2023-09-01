package com.example.sewinglessons.app

import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.sewinglessons.HomeViewModel
import com.example.sewinglessons.PatternDetailsViewModel
import com.example.sewinglessons.ProjectDetailsViewModel
import com.example.sewinglessons.navigation.Screen
import com.example.sewinglessons.navigation.SewingAppRouter
import com.example.sewinglessons.screens.AccountScreen
import com.example.sewinglessons.screens.AddProjectScreen
import com.example.sewinglessons.screens.FavoritesScreen
import com.example.sewinglessons.screens.HomeScreen
import com.example.sewinglessons.screens.LoginScreen
import com.example.sewinglessons.screens.PatternDetailsScreen
import com.example.sewinglessons.screens.ProjectDetailsScreen
import com.example.sewinglessons.screens.SearchScreen
import com.example.sewinglessons.screens.SettingsScreen
import com.example.sewinglessons.screens.SignUpScreen

@Composable
fun SewingApp(intent: Intent, homeViewModel: HomeViewModel = viewModel(), projectDetailsViewModel: ProjectDetailsViewModel = viewModel()) {

    homeViewModel.checkForActiveSession()

    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color.White) {

        if (homeViewModel.isUserLoggedIn.value == true) {
            SewingAppRouter.navigateTo(Screen.HomeScreen)
        }

        val notificationAction = intent?.action
        if (notificationAction == "com.example.sewinglessons.screens") {
            if (homeViewModel.isUserLoggedIn.value == true) {
                SewingAppRouter.navigateTo(Screen.FavoritesScreen)
            }
            else {
                SewingAppRouter.navigateTo(Screen.SignUpScreen)
            }
        }

        Crossfade(targetState = SewingAppRouter.currentScreen) { currentState ->
            when (currentState.value) {
                is Screen.SignUpScreen -> {
                    SignUpScreen()
                }
                is Screen.LoginScreen -> {
                    LoginScreen()
                }
                is Screen.HomeScreen -> {
                    HomeScreen()
                }
                is Screen.SearchScreen -> {
                    SearchScreen()
                }
                is Screen.FavoritesScreen -> {
                    FavoritesScreen()
                }
                is Screen.SettingsScreen -> {
                    SettingsScreen()
                }
                is Screen.AccountScreen -> {
                    AccountScreen()
                }
                is Screen.AddProjectScreen -> {
                    AddProjectScreen()
                }
                is Screen.PatternDetailsScreen -> {
                    val patternItem = (currentState.value as Screen.PatternDetailsScreen).patternItem
                    PatternDetailsScreen(patternItem)
                }
                is Screen.ProjectDetailsScreen -> {
                    val title = (currentState.value as Screen.ProjectDetailsScreen).title
                    val description = (currentState.value as Screen.ProjectDetailsScreen).description
                    val painter = (currentState.value as Screen.ProjectDetailsScreen).painter
                    ProjectDetailsScreen(projectDetailsViewModel, title, description, painter)
                }
            }
            
        }
    }
}