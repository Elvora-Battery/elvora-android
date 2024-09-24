package com.unsoed.elvora.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unsoed.elvora.data.repository.HomeRepository
import com.unsoed.elvora.di.Injection
import com.unsoed.elvora.ui.home.HomeViewModel

class HomeModelFactory(private val homeRepository: HomeRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(homeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: HomeModelFactory? = null

        fun getInstance(context: Context): HomeModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = HomeModelFactory(Injection.provideHomeRepository(context))
                INSTANCE = instance
                instance
            }
        }
    }
}