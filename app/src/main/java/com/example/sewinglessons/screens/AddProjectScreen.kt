package com.example.sewinglessons.screens

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sewinglessons.AddProjectViewModel
import com.example.sewinglessons.LoginViewModel
import com.example.sewinglessons.R
import com.example.sewinglessons.components.DescriptionTextField
import com.example.sewinglessons.navigation.Screen
import com.example.sewinglessons.navigation.SewingAppRouter
import com.example.sewinglessons.navigation.SystemBackButtonHandler
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import com.example.sewinglessons.components.TitleTextField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import java.util.UUID

@Composable
fun AddProjectScreen(addProjectViewModel: AddProjectViewModel = viewModel()) {
    val titleState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val db = Firebase.firestore
    val userUid = FirebaseAuth.getInstance().currentUser?.uid

    Surface (
        color = colorResource(id = R.color.colorPrimary),
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimary))
            .padding(24.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
                tint = colorResource(id = R.color.colorWhite),
                modifier = Modifier
                    .size(35.dp)
                    .padding(start = 4.dp, top = 5.dp)
                    .clickable {
                        SewingAppRouter.navigateTo(Screen.AccountScreen)
                    }
            )
        }
        Box {
            val isUploading = remember { mutableStateOf(false) }
            val context = LocalContext.current
            val bitmap = remember { mutableStateOf<Bitmap?>(null) }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicturePreview()
            ) {
                if (it != null) {
                    bitmap.value = it
                }
            }

            val launchImage = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->
                if (uri != null) {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    } else {
                        val source = uri.let { it1 ->
                            ImageDecoder.createSource(context.contentResolver, it1)
                        }
                        bitmap.value = source?.let { it1 ->
                            ImageDecoder.decodeBitmap(it1)
                        }
                    }
                } else {
                    bitmap.value = null
                }
            }

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp)
            ) {
                if (bitmap.value != null) {
                    Image(
                        bitmap = bitmap.value!!.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(250.dp)
                            .background(Color.White)
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                    )
                } else {

                    Image(
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(250.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                    )
                }
            }
            var showDialog = remember { mutableStateOf(false) }
            Box (modifier = Modifier.padding(top = 280.dp, start = 260.dp)) {
                Image(painter = painterResource(
                    id = R.drawable.baseline_photo_camera_24),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .size(50.dp)
                        .padding(10.dp)
                        .clickable {
                            showDialog.value = true
                        })
            }
            Column (horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 250.dp)
            ){
                TitleTextField(labelValue = stringResource(id = R.string.title),
                    onTextSelected = { text ->
                        titleState.value = text
                    })
                Spacer(modifier = Modifier.height(10.dp))
                DescriptionTextField(labelValue = stringResource(id = R.string.description),
                    onTextSelected = { text ->
                        descriptionState.value = text
                    })
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    isUploading.value = true
                    bitmap.value?.let { bitmap ->
                        addProjectViewModel.uploadImageToFirebase(bitmap, titleState.value, descriptionState.value, db, userUid.toString(), context as ComponentActivity) { success ->
                            isUploading.value = false
                            if (success) {
                                Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                                SewingAppRouter.navigateTo(Screen.AccountScreen)
                            } else {
                                Toast.makeText(context, "Failed to Upload", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = colorResource(id = R.color.colorPrimary)
                    ), enabled = bitmap.value != null && titleState.value.isNotBlank() && descriptionState.value.isNotBlank()
                    ) {
                    Text(
                        text = stringResource(id = R.string.add),
                        fontSize = 20.sp,
                    )
                }
            }
            Column (verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp)) {
                if (showDialog.value) {
                    Row (verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .width(300.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Gray)) {
                        Column (modifier = Modifier.padding(start = 60.dp)) {
                            Image(painter = painterResource(
                                id = R.drawable.baseline_photo_camera_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        launcher.launch()
                                        showDialog.value = false
                                    }
                            )
                            Text(text = stringResource(id = R.string.camera),
                                color = Color.White)
                        }
                        Spacer(modifier = Modifier.padding(30.dp))
                        Column {
                            Image(painter = painterResource(
                                id = R.drawable.baseline_image_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        launchImage.launch("image/*")
                                        showDialog.value = false
                                    }

                            )
                            Text(text = stringResource(id = R.string.gallery),
                                color = Color.White)
                        }
                        Column (modifier = Modifier
                            .padding(start = 50.dp, bottom = 80.dp)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = R.string.exit),
                                tint = colorResource(id = R.color.colorWhite),
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        showDialog.value = false
                                    }
                            )
                        }
                    }
                }
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(450.dp)
                    .fillMaxWidth()
            ) {
                if (isUploading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp),
                        color = Color.White
                    )
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
fun AddProjectScreenPreview() {
    AddProjectScreen()
}