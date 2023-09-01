package com.example.sewinglessons.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sewinglessons.HomeViewModel
import com.example.sewinglessons.PatternImageCard
import com.example.sewinglessons.components.AppToolbar
import com.example.sewinglessons.R
import com.example.sewinglessons.components.AppBottomNavigation
import com.example.sewinglessons.data.api.model.PatternItem
import com.example.sewinglessons.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val state by homeViewModel.state.collectAsState()
    
    Scaffold (
        topBar = {
            AppToolbar(toolbarTitle = stringResource(id = R.string.app_name),
                logoutButtonClicked = {
                    homeViewModel.logout()
                })
        },
        bottomBar = {
            AppBottomNavigation(
                selectedTab = Screen.HomeScreen,
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

            }
            LazyColumn {
                if (state.isEmpty()) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                }
                items(state) { pattern: PatternItem ->
                        PatternImageCard(pattern = pattern)
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}