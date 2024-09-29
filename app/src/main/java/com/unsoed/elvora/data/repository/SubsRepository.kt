package com.unsoed.elvora.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.UserShippingModel
import com.unsoed.elvora.data.local.UserPreferences
import com.unsoed.elvora.data.network.ApiService
import com.unsoed.elvora.data.response.CommonResponse
import com.unsoed.elvora.data.response.active.DataItem
import com.unsoed.elvora.data.response.getSubs.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class SubsRepository(private val apiService: ApiService, private val dataStore: UserPreferences) {
    fun getUserSession(): LiveData<UserModel> {
        return dataStore.getUser().asLiveData()
    }

    fun getUserShipping(): LiveData<UserShippingModel> {
        return dataStore.getUserShipping().asLiveData()
    }
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
                        Log.d(TAG, "Response : ${response.toString()}")
                        Log.d(TAG,"Response Body : ${responseBody.data.toString()}")
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

    fun getTransactionById(id: Int): LiveData<ApiResult<com.unsoed.elvora.data.response.transactionId.Data>> {
        return liveData {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getUser().firstOrNull()
            }

            try {
                emit(ApiResult.Loading)
                val response = apiService.getTransactionById(
                    token = "Bearer ${tokenUser?.token}",
                    id = id
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
    fun getActiveSubs(): LiveData<ApiResult<List<DataItem>>> {
        return liveData {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getUser().firstOrNull()
            }

            try {
                emit(ApiResult.Loading)
                val response = apiService.getActiveSubs(
                    token = "Bearer ${tokenUser?.token}",
                )
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        emit(ApiResult.Success(responseBody.data!!))
                    }
                } else {
                    Log.e(TAG, "Error getActiveSubs")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message ?: "Unknown Error getActiveSubs"))
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
                    Log.e(TAG, "Error ")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message ?: "Unknown Error changeBatteryName"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error Ganti Nama Baterai ${e.message}")
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