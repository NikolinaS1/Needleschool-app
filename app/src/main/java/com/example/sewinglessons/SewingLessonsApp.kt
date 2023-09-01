package com.example.sewinglessons

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SewingLessonsApp : Application () {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}