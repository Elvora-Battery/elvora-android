package com.unsoed.elvora.di

import android.content.Context
import com.unsoed.elvora.data.local.UserPreferences
import com.unsoed.elvora.data.local.dataStore
import com.unsoed.elvora.data.network.ApiConfig
import com.unsoed.elvora.data.repository.AuthRepository
import com.unsoed.elvora.data.repository.HomeRepository
import com.unsoed.elvora.data.repository.RentRepository
import com.unsoed.elvora.data.repository.SubsRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreferences.getInstanceDataStore(context.dataStore)
        return AuthRepository.getInstance(apiService, dataStore)
    }

    fun provideHomeRepository(context: Context): HomeRepository {
        val apiService = ApiConfig.getApiService()
        val apiServiceRecommendation = ApiConfig.getApiServiceRecommendation()
        val dataStore = UserPreferences.getInstanceDataStore(context.dataStore)
        return HomeRepository.getInstance(apiService, dataStore, apiServiceRecommendation)
    }

    fun provideRentRepository(context: Context): RentRepository {
        val apiService = ApiConfig.getApiService()
        val apiServiceScan = ApiConfig.getApiServiceScan()
        val dataStore = UserPreferences.getInstanceDataStore(context.dataStore)
        return RentRepository.getInstance(apiService, dataStore, apiServiceScan)
    }
    fun provideSubsRepository(context: Context): SubsRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreferences.getInstanceDataStore(context.dataStore)
        return SubsRepository.getInstance(apiService, dataStore)
    }
}