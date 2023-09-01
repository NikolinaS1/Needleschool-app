package com.example.sewinglessons.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sewinglessons.HomeViewModel
import com.example.sewinglessons.PatternImageCard
import com.example.sewinglessons.components.AppToolbar
import com.example.sewinglessons.data.SignUpViewModel
import com.example.sewinglessons.R
import com.example.sewinglessons.SearchBar
import com.example.sewinglessons.components.AppBottomNavigation
import com.example.sewinglessons.components.ButtonComponent
import com.example.sewinglessons.components.NormalTextComponent
import com.example.sewinglessons.components.TextComponent
import com.example.sewinglessons.components.TitleTextComponent
import com.example.sewinglessons.data.api.model.PatternItem
import com.example.sewinglessons.navigation.Screen
import com.example.sewinglessons.navigation.SewingAppRouter
import com.example.sewinglessons.navigation.SystemBackButtonHandler
import kotlinx.coroutines.flow.map

@SuppressLint("FlowOperatorInvokedInComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(homeViewModel: HomeViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var filteredPatterns by remember { mutableStateOf(emptyList<PatternItem>()) }

    DisposableEffect(searchQuery, isSearching) {
        if (isSearching) {
            val uppercaseQuery = searchQuery.toUpperCase() // Pretvori u velika slova
            filteredPatterns = homeViewModel.state.value.filter { pattern ->
                pattern.name.contains(uppercaseQuery)
            }
        } else {
            filteredPatterns = emptyList() // Clear the list when not searching
        }

        onDispose {} // Cleanup
    }

    Scaffold (
        topBar = {
            AppToolbar(toolbarTitle = stringResource(id = R.string.app_name),
                logoutButtonClicked = {
                    homeViewModel.logout()
                })
        },
        bottomBar = {
            AppBottomNavigation(
                selectedTab = Screen.SearchScreen,
                onTabSelected = { /* Handle tab selection */ }
            )
        }
    ) { paddingValues ->
        Surface (
            color = colorResource(id = R.color.colorPrimary),
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.colorPrimary))
                .padding(paddingValues)
        ) {
            Column (modifier = Modifier.fillMaxSize()) {
                SearchBar(onSearch = { newSearchQuery ->
                    searchQuery = newSearchQuery
                    isSearching = true // Update isSearching when search is triggered
                })

                // Display filtered patterns only if isSearching is true
                if (isSearching) {
                    LazyColumn {
                        if (filteredPatterns.isEmpty()) {
                            item {
                                TextComponent(value = "No results found")
                            }
                        }
                        items(filteredPatterns) { pattern: PatternItem ->
                            PatternImageCard(pattern = pattern)
                        }
                    }
                }
            }
        }
    }
    SystemBackButtonHandler {
        SewingAppRouter.navigateTo(Screen.SearchScreen)
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}