package com.unsoed.elvora.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unsoed.elvora.data.repository.SubsRepository
import com.unsoed.elvora.di.Injection
import com.unsoed.elvora.ui.subs.SubsViewModel

class SubsModelFactory(private val subsRepository: SubsRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SubsViewModel::class.java)) {
            return SubsViewModel(subsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: SubsModelFactory? = null

        fun getInstance(context: Context): SubsModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = SubsModelFactory(Injection.provideSubsRepository(context))
                INSTANCE = instance
                instance
            }
        }
    }
}