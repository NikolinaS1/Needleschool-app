package com.example.sewinglessons

import android.content.ContentValues
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class AddProjectViewModel : ViewModel() {

    fun uploadImageToFirebase(bitmap: Bitmap, title: String, description: String, db: FirebaseFirestore, currentUserUid: String, context: ComponentActivity, callback: (Boolean) -> Unit) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        imageRef.putBytes(imageData)
            .continueWithTask { uploadTask ->
                if (!uploadTask.isSuccessful) {
                    uploadTask.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }
            .addOnCompleteListener { downloadUrlTask ->
                if (downloadUrlTask.isSuccessful) {
                    val imageUrl = downloadUrlTask.result.toString()

                    uploadTextToFirebase(imageUrl, title, description, db, currentUserUid)

                    callback(true)
                } else {
                    callback(false)
                }
            }
    }


    fun uploadTextToFirebase(image: String, title: String, description: String, db: FirebaseFirestore, currentUserUid: String) {
        val projectData = hashMapOf(
            "title" to title,
            "description" to description,
            "image" to image
        )
        db.collection("user_projects")
            .document(currentUserUid)
            .collection("projects")
            .add(projectData)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "Project added with ID: ${documentReference.id}")
            }
            .addOnFailureListener {e ->
                Log.e(ContentValues.TAG, "Error")
            }
    }
}