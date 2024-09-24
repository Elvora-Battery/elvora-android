package com.unsoed.elvora.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unsoed.elvora.data.repository.RentRepository
import com.unsoed.elvora.di.Injection
import com.unsoed.elvora.ui.rent.RentViewModel

class RentModelFactory(private val rentRepository: RentRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RentViewModel::class.java)) {
            return RentViewModel(rentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: RentModelFactory? = null

        fun getInstance(context: Context): RentModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = RentModelFactory(Injection.provideRentRepository(context))
                INSTANCE = instance
                instance
            }
        }
    }
}