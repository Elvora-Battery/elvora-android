package com.unsoed.elvora.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unsoed.elvora.data.repository.AuthRepository
import com.unsoed.elvora.di.Injection
import com.unsoed.elvora.ui.auth.AuthViewModel

class ViewModelFactory(private val authRepository: AuthRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = ViewModelFactory(Injection.provideAuthRepository(context))
                INSTANCE = instance
                instance
            }
        }
    }
}