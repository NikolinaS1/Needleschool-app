package com.example.sewinglessons

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sewinglessons.data.api.model.PatternItem
import com.example.sewinglessons.data.repository.PatternRepo
import com.example.sewinglessons.navigation.Screen
import com.example.sewinglessons.navigation.SewingAppRouter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val patternRepo: PatternRepo
): ViewModel() {
    private val TAG = HomeViewModel::class.simpleName
    private val _state = MutableStateFlow(emptyList<PatternItem>())
    val state: StateFlow<List<PatternItem>>
        get() = _state

    init {
        viewModelScope.launch {
            val patterns = patternRepo.getPatterns()
            _state.value = patterns
        }
    }

    val isUserLoggedIn : MutableLiveData<Boolean> = MutableLiveData()

    fun logout () {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()

        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                Log.d(TAG, "Inside sign out success")
                SewingAppRouter.navigateTo(Screen.LoginScreen)
            } else {
                Log.d(TAG, "Inside sign out is not complete")
            }
        }
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun checkForActiveSession() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d(TAG, "Valid session")
            isUserLoggedIn.value = true
        } else {
            Log.d(TAG, "User is not logged in")
            isUserLoggedIn.value = false
        }
    }
}