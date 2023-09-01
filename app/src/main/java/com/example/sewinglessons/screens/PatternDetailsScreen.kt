package com.example.sewinglessons.screens

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.sewinglessons.MainActivity
import com.example.sewinglessons.PatternDetailsViewModel
import com.example.sewinglessons.R
import com.example.sewinglessons.components.CardTextComponent
import com.example.sewinglessons.components.CardTitleTextComponent
import com.example.sewinglessons.components.NameTextComponent
import com.example.sewinglessons.data.api.model.PatternItem
import com.example.sewinglessons.navigation.SewingAppRouter
import com.example.sewinglessons.navigation.SystemBackButtonHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@SuppressLint("UnrememberedMutableState")
@Composable
fun PatternDetailsScreen(pattern: PatternItem, patternDetailsViewModel: PatternDetailsViewModel = viewModel()) {
    patternDetailsViewModel.setSelectedPatternItem(pattern)
    val imagerPainter = rememberImagePainter(data = pattern.image)

    val selectedPatternItem by patternDetailsViewModel.selectedPatternItem
    val firestore = Firebase.firestore
    val userUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    val context = LocalContext.current
    val channelId = "MyTestChannel"
    val notificationId = 0

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    var isRed by remember {
        mutableStateOf(
            sharedPreferences.getBoolean("is_pattern_in_firebase_${pattern.id}", false)
        )
    }

    LaunchedEffect(Unit) {
        createNotificationChannel(channelId, context)
    }

    Surface (
        color = colorResource(id = R.color.colorWhite),
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorWhite))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            selectedPatternItem?.let { patternItem ->
                LazyColumn {
                    item {
                        Column {
                            Image(
                                painter = imagerPainter,
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            NameTextComponent(value = patternItem.name)
                            Spacer(modifier = Modifier.padding(5.dp))
                            IconButton(
                                onClick = {
                                    if (!isRed) {
                                        selectedPatternItem?.let { patternItem ->
                                            firestore.collection("user_patterns")
                                                .document(userUid)
                                                .collection("patterns")
                                                .add(patternItem)
                                                .addOnSuccessListener {
                                                    showSimpleNotification(
                                                        context,
                                                        channelId,
                                                        notificationId,
                                                        "You added a pattern to your favorites",
                                                        "Tap to check your favorites"
                                                    )
                                                    isRed = true
                                                    sharedPreferences.edit()
                                                        .putBoolean("is_pattern_in_firebase_${pattern.id}", true)
                                                        .apply()
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e(TAG, "Error adding pattern: $e")
                                                }
                                        }
                                    }
                                    else {
                                        selectedPatternItem?.let { patternItem ->
                                            firestore.collection("user_patterns")
                                                .document(userUid)
                                                .collection("patterns")
                                                .whereEqualTo("id", patternItem.id)
                                                .get()
                                                .addOnSuccessListener { querySnapshot ->
                                                    for (document in querySnapshot.documents) {
                                                        document.reference.delete()
                                                    }
                                                    isRed = false
                                                    sharedPreferences.edit()
                                                        .putBoolean("is_pattern_in_firebase_${pattern.id}", false)
                                                        .apply()
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e(TAG, "Error deleting pattern: $e")
                                                }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .wrapContentSize(Alignment.Center)
                            ) {
                                Icon(imageVector = if(isRed) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if(isRed) Color.Red else Color.Black,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                            Spacer(modifier = Modifier.padding(5.dp))
                            CardTextComponent(value = patternItem.shortDescription)
                            Spacer(modifier = Modifier.padding(15.dp))
                            CardTitleTextComponent(value = stringResource(id = R.string.materials_used))
                            Spacer(modifier = Modifier.padding(5.dp))
                            CardTextComponent(value = patternItem.materialsUsed)
                            Spacer(modifier = Modifier.padding(15.dp))
                            CardTitleTextComponent(value = stringResource(id = R.string.recommended_fabrics))
                            Spacer(modifier = Modifier.padding(5.dp))
                            CardTextComponent(value = patternItem.recommendedFabrics)
                            Spacer(modifier = Modifier.padding(15.dp))
                            CardTitleTextComponent(value = stringResource(id = R.string.process))
                            Spacer(modifier = Modifier.padding(5.dp))
                            CardTextComponent(value = patternItem.description)
                            Spacer(modifier = Modifier.padding(15.dp))
                        }
                    }
                }
            }
        }
    }
    SystemBackButtonHandler {
        SewingAppRouter.navigateBack()
    }
}


fun createNotificationChannel(channelId: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "MyTestChannel"
        val descriptionText = "My important test channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@SuppressLint("MissingPermission")
fun showSimpleNotification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val intent = Intent(context, MainActivity::class.java)
    intent.action = "com.example.sewinglessons.screens"

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.baseline_favorite_24)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(priority)
        .setContentIntent(pendingIntent)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}


