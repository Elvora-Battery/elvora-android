package com.unsoed.elvora.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.local.UserPreferences
import com.unsoed.elvora.data.network.ApiService
import com.unsoed.elvora.data.response.CommonResponse
import com.unsoed.elvora.data.response.getSubs.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class SubsRepository(private val apiService: ApiService, private val dataStore: UserPreferences) {

    fun getAllSubs(): LiveData<ApiResult<Data>> {
        return liveData {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getUser().firstOrNull()
            }

            try {
                emit(ApiResult.Loading)
                val response = apiService.getAllSubscription(
                    token = "Bearer ${tokenUser?.token}",
                )
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        emit(ApiResult.Success(responseBody.data!!))
                    }
                } else {
                    Log.e(TAG, "Error Get Subs")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message ?: "Unknown Error get All subs"))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }
    fun changeBatteryName(id: Int, name: String): LiveData<ApiResult<String>> {
        return liveData {
            try {
                emit(ApiResult.Loading)
                val response = apiService.changeBatteryName(
                    id = id,
                    batteryName = name,
                )
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        emit(ApiResult.Success(responseBody.message!!))
                    }
                } else {
                    Log.e(TAG, "Error changeBatteryName")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message ?: "Unknown Error changeBatteryName"))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }
    companion object {
        private const val TAG = "SubsRepository"

        @Volatile
        private var INSTANCE: SubsRepository? = null

        fun getInstance(apiService: ApiService, dataStore: UserPreferences): SubsRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = SubsRepository(apiService = apiService, dataStore = dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}