package com.example.sewinglessons.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sewinglessons.AccountViewModel
import com.example.sewinglessons.ProjectDetailsViewModel
import com.example.sewinglessons.R
import com.example.sewinglessons.components.AddTextComponent
import com.example.sewinglessons.components.DetailsTextComponent
import com.example.sewinglessons.data.ProjectData
import com.example.sewinglessons.navigation.Screen
import com.example.sewinglessons.navigation.SewingAppRouter
import com.example.sewinglessons.navigation.SystemBackButtonHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@SuppressLint("UnrememberedMutableState")
@Composable
fun ProjectDetailsScreen(projectDetailsViewModel: ProjectDetailsViewModel, title: String, description: String, painter: Painter) {

    Surface(
        color = colorResource(id = R.color.colorWhite),
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorWhite))
            .padding(24.dp)
    ) {
        Column () {
            Spacer(modifier = Modifier.padding(30.dp))
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(20.dp))
            DetailsTextComponent(title = "Title:", value = title)
            Spacer(modifier = Modifier.padding(15.dp))
            DetailsTextComponent(title = "Description:", value = description)
        }
    }
    SystemBackButtonHandler {
        SewingAppRouter.navigateTo(Screen.AccountScreen)
    }
}
