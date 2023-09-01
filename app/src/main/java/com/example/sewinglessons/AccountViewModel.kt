package com.example.sewinglessons

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.sewinglessons.data.ProjectData
import com.example.sewinglessons.navigation.Screen
import com.example.sewinglessons.navigation.SewingAppRouter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountViewModel : ViewModel () {
    val emailId: MutableState<String?> = mutableStateOf(null)

    fun getUserData() {
        FirebaseAuth.getInstance().currentUser?.also {
            it.email?.also { email ->
                emailId.value = email
            }
        }
    }

    fun retrieveDataFromFirestore(db: FirebaseFirestore, currentUserUid: String, callback: (List<ProjectData>) -> Unit) {
        val projectsCollection = db.collection("user_projects")
            .document(currentUserUid)
            .collection("projects")
        projectsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val projects = querySnapshot.documents.mapNotNull { documentSnapshot ->
                    val projectData = documentSnapshot.toObject(ProjectData::class.java)
                    projectData
                }
                callback(projects)
            }
            .addOnFailureListener { e ->
                Log.e(ContentValues.TAG, "Error fetching projects", e)
                callback (emptyList())
            }
    }

    fun deleteProjectFromFirestore(projectId: String, db: FirebaseFirestore, currentUserUid: String, callback: (Boolean) -> Unit) {
        db.collection("user_projects")
            .document(currentUserUid)
            .collection("projects")
            .document(projectId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Project deleted successfully")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e(ContentValues.TAG, "Error deleting project", e)
                callback(false)
            }
    }

    fun getProjectId(title: String?, description: String?, image: String?, currentUserUid: String, db: FirebaseFirestore, callback: (String?) -> Unit) {
        db.collection("user_projects")
            .document(currentUserUid)
            .collection("projects")
            .whereEqualTo("title", (title ?: "-"))
            .whereEqualTo("description", (description ?: "-"))
            .whereEqualTo("image", (image ?: "-"))
            .get()
            .addOnSuccessListener { querySnapshot ->
                if(!querySnapshot.isEmpty) {
                    val projectId = querySnapshot.documents[0].id
                    callback(projectId)
                }
                else {
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error retrieving project ID.", e)
                callback(null)
            }
    }


    @Composable
    fun ImageCard(
        painter: Painter,
        contentDescription: String?,
        title: String,
        description: String,
        modifier: Modifier = Modifier,
        onCloseClicked: () -> Unit
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        ) {
            Box(modifier = Modifier.height(200.dp)) {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                        .clickable {
                            SewingAppRouter.navigateTo(Screen.ProjectDetailsScreen(title, description, painter))
                        }
                )
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black
                                ),
                                startY = 300f
                            )
                        )
                )
                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(2.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = onCloseClicked) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.exit),
                            tint = Color.Black
                        )
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        title,
                        style = TextStyle(color = Color.White, fontSize = 16.sp)
                    )
                }
            }
        }
    }
}