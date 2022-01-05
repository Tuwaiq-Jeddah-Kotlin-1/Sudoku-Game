package com.renad.sudoku.ui.main_activity.fragments.settings_fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renad.sudoku.firebase.Auth
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    //google signIn functions

    private var auth = Auth()

    fun configureGoogleSign(context: Context) {
        viewModelScope.launch {
            auth.configureGoogleSignIn(context)
        }
    }

    fun signIn(activity: Activity) {
        viewModelScope.launch {
            auth.signIn(activity)
        }
    }

    fun checkGoogleSignIn(requestCode: Int, activity: Activity,context: Context,data:Intent?) {
        viewModelScope.launch {
            auth.checkGoogleSignIn(requestCode, activity,context,data)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            auth.signOut()
        }
    }

    fun firebaseAuthWithGoogle(idToken: String, activity: Activity,context: Context) {
        viewModelScope.launch {
            auth.firebaseAuthWithGoogle(idToken, activity,context)
        }
    }

    fun getRequestCode(): Int {
        return auth.getRequestCode()
    }
}