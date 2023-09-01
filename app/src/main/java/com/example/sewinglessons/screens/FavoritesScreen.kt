package com.example.sewinglessons.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sewinglessons.FavoritesViewModel
import com.example.sewinglessons.HomeViewModel
import com.example.sewinglessons.PatternImageCard
import com.example.sewinglessons.components.AppToolbar
import com.example.sewinglessons.R
import com.example.sewinglessons.components.AppBottomNavigation
import com.example.sewinglessons.data.api.model.PatternItem
import com.example.sewinglessons.navigation.Screen
import com.example.sewinglessons.navigation.SewingAppRouter
import com.example.sewinglessons.navigation.SystemBackButtonHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(homeViewModel: HomeViewModel = viewModel(), favoritesViewModel: FavoritesViewModel = viewModel()) {
    val userUid = FirebaseAuth.getInstance().currentUser?.uid
    val db = Firebase.firestore

    var patternItems by remember { mutableStateOf<List<PatternItem>>(emptyList()) }
    var loadingInProgress = mutableStateOf(true)

    favoritesViewModel.retrieveDataFromFirestore(db, userUid.toString()) { patterns ->
        patternItems = patterns
        loadingInProgress.value = false
    }


    Scaffold(
        topBar = {
            AppToolbar(
                toolbarTitle = stringResource(id = R.string.app_name),
                logoutButtonClicked = {
                    homeViewModel.logout()
                }
            )
        },
        bottomBar = {
            AppBottomNavigation(
                selectedTab = Screen.FavoritesScreen,
                onTabSelected = { /* Handle tab selection */ }
            )
        }
    ) { paddingValues ->

        Surface(
            color = colorResource(id = R.color.colorPrimary),
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.colorPrimary))
                .padding(paddingValues)
        ) {
            if (!loadingInProgress.value) {
                LazyColumn {
                    items(patternItems) { pattern ->
                        PatternImageCard(pattern = pattern)
                    }
                }
            }
        }
        SystemBackButtonHandler {
            SewingAppRouter.navigateTo(Screen.FavoritesScreen)
        }
    }
}



