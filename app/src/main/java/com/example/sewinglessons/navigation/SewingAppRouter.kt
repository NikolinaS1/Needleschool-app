package com.example.sewinglessons.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import com.example.sewinglessons.data.api.model.PatternItem

sealed class Screen {

    object SignUpScreen : Screen()
    object LoginScreen : Screen()
    object HomeScreen : Screen()
    object FavoritesScreen: Screen()
    object SearchScreen: Screen()
    object SettingsScreen: Screen()
    object AccountScreen: Screen()
    object AddProjectScreen: Screen()
    data class PatternDetailsScreen(val patternItem: PatternItem) : Screen()
    data class ProjectDetailsScreen(val title: String, val description: String, val painter: Painter) : Screen()
}


object SewingAppRouter {

    private val screenStack: MutableList<Screen> = mutableListOf()

    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.SignUpScreen)

    init {
        // Initialize the stack with the initial screen
        screenStack.add(Screen.SignUpScreen)
    }

    fun navigateTo(destination: Screen) {
        currentScreen.value = destination
        screenStack.add(destination)
    }

    fun navigateBack() {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
            val previousScreen = screenStack.last()
            currentScreen.value = previousScreen
        }
    }
}