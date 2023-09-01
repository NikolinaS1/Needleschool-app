package com.example.sewinglessons.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sewinglessons.AccountViewModel
import com.example.sewinglessons.HomeViewModel
import com.example.sewinglessons.components.AppToolbar
import com.example.sewinglessons.R
import com.example.sewinglessons.components.AppBottomNavigation
import com.example.sewinglessons.navigation.Screen
import com.example.sewinglessons.navigation.SewingAppRouter
import com.example.sewinglessons.navigation.SystemBackButtonHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import coil.compose.rememberImagePainter
import com.example.sewinglessons.components.AddTextComponent
import com.example.sewinglessons.components.TextComponent
import com.example.sewinglessons.data.ProjectData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(homeViewModel: HomeViewModel = viewModel(),
                  accountViewModel: AccountViewModel = viewModel()) {

    accountViewModel.getUserData()
    val userUid = FirebaseAuth.getInstance().currentUser?.uid
    val db = Firebase.firestore
    val projectsListState = remember { mutableStateOf<List<ProjectData>>(emptyList()) }
    val scrollState = rememberScrollState()
    var loadingInProgress = mutableStateOf(true)
    var deletedProjectId by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            AppToolbar(toolbarTitle = stringResource(id = R.string.app_name),
                logoutButtonClicked = {
                    homeViewModel.logout()
                })
        },
        bottomBar = {
            AppBottomNavigation(
                selectedTab = Screen.AccountScreen,
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
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.settings),
                    tint = colorResource(id = R.color.colorWhite),
                    modifier = Modifier
                        .size(43.dp)
                        .padding(top = 8.dp, end = 14.dp)
                        .clickable {
                            SewingAppRouter.navigateTo(Screen.SettingsScreen)
                        }
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(150.dp) // Set the size of the image
                        .padding(vertical = 16.dp), // Add vertical padding
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White)
                )
                TextComponent(value = accountViewModel.emailId?.value ?: "")
                Spacer(modifier = Modifier.height(80.dp))
                AddTextComponent(value = stringResource(id = R.string.my_projects))

                Button(onClick = {
                    SewingAppRouter.navigateTo(Screen.AddProjectScreen)
                }, modifier = Modifier
                    .align(Alignment.Start)
                    .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = colorResource(id = R.color.colorPrimary))) {
                    Text(text = stringResource(id = R.string.addNew))
                }

                accountViewModel.retrieveDataFromFirestore(db, userUid.toString()) { projects ->
                    projectsListState.value = projects
                    loadingInProgress.value = false
                }

                if (!loadingInProgress.value) {
                    val items = projectsListState.value

                    var rowIndex = 0

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        while (rowIndex < items.size) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                for (i in rowIndex until rowIndex + 2) {
                                    if (i < items.size) {
                                        val project = items[i]
                                        val imagePainter = rememberImagePainter(data = project.image)
                                        val title = "${project.title ?: ""}"
                                        val description = "${project.description ?: ""}"
                                        val image = "${project.image ?: null}"

                                        Box(
                                            modifier = Modifier
                                                .width((LocalConfiguration.current.screenWidthDp * 0.4).dp)
                                                .padding(4.dp)
                                                .clickable {
                                                    // Handle click on item
                                                }
                                        ) {
                                            accountViewModel.ImageCard(
                                                painter = imagePainter,
                                                contentDescription = null,
                                                title = title,
                                                description = description,
                                                onCloseClicked = {
                                                    accountViewModel.getProjectId(title, description, image, userUid.toString(), db) {projectId ->
                                                        if (projectId != null) {
                                                            deletedProjectId = projectId
                                                        }
                                                    }
                                                    showDialog = true
                                                }
                                            )
                                        }
                                    } else {
                                        Spacer(
                                            modifier = Modifier
                                                .width((LocalConfiguration.current.screenWidthDp * 0.4).dp)
                                                .padding(4.dp)
                                        )
                                    }
                                }
                            }
                            rowIndex += 2

                            if (rowIndex == 2) {
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
                else {
                    CircularProgressIndicator()
                }

                if (showDialog) {
                    if (deletedProjectId != null) {
                        AlertDialog(
                            onDismissRequest = {
                                showDialog = false
                            },
                            title = {
                                Text(text = "Deleting project")
                            },
                            text = {
                                Text(text = "Are you sure you want to delete your project?")
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(
                                            id = R.color.colorPrimary
                                        ), contentColor = Color.White
                                    )
                                ) {
                                    Text(text = "No")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        accountViewModel.deleteProjectFromFirestore(
                                            deletedProjectId.toString(),
                                            db,
                                            userUid.toString()
                                        ) { success ->
                                            if (success) {
                                                showDialog = false
                                                deletedProjectId = null
                                            } else {
                                                Log.e(TAG, "Project deletion failed")
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(
                                            id = R.color.colorPrimary
                                        ), contentColor = Color.White
                                    )
                                ) {
                                    Text(text = "Yes")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    SystemBackButtonHandler {
        SewingAppRouter.navigateTo(Screen.AccountScreen)
    }
}

@Preview
@Composable
fun AccountScreenPreview() {
    AccountScreen()
}