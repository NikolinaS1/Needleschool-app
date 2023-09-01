package com.example.sewinglessons

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sewinglessons.data.api.model.PatternItem
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesViewModel : ViewModel() {

    fun retrieveDataFromFirestore(db: FirebaseFirestore, currentUserUid: String, callback: (List<PatternItem>) -> Unit) {
        val projectsCollection = db.collection("user_patterns")
            .document(currentUserUid)
            .collection("patterns")
        projectsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val patterns = querySnapshot.documents.mapNotNull { documentSnapshot ->
                    val patternItem = documentSnapshot.toObject(PatternItem::class.java)
                    patternItem
                }
                callback(patterns)
            }
            .addOnFailureListener { e ->
                Log.e(ContentValues.TAG, "Error fetching projects", e)
                callback (emptyList())
            }
    }
}