package com.example.sewinglessons

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SettingsViewModel : ViewModel() {

    fun sendPasswordResetEmail(email: String, onEmailSent: (Boolean) -> Unit) {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onEmailSent(true)
                } else {
                    onEmailSent(false)
                }
            }
    }
}
