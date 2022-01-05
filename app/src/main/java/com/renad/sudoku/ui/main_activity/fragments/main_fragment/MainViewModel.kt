package com.renad.sudoku.ui.main_activity.fragments.main_fragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.renad.sudoku.firebase.Auth
import kotlinx.coroutines.launch

class MainViewModel(context: Application) : AndroidViewModel(context) {
    private var auth= Auth()

    fun anonymousSignIn(context: Context){
        viewModelScope.launch {
            auth.anonymousSignIn(context)
        }
    }
}