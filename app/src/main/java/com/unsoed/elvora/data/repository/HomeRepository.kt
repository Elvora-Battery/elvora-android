package com.unsoed.elvora.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.MapRequest
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.UserShippingModel
import com.unsoed.elvora.data.UserVerify
import com.unsoed.elvora.data.local.UserPreferences
import com.unsoed.elvora.data.network.ApiService
import com.unsoed.elvora.data.response.CommonResponse
import com.unsoed.elvora.data.response.map.MapResponse

class HomeRepository(
    private val apiService: ApiService,
    private val dataStore: UserPreferences,
    private val apiServiceRecommendation: ApiService
) {

    fun getUserSession(): LiveData<UserModel> {
        return dataStore.getUser().asLiveData()
    }

    fun getUserShipping(): LiveData<UserShippingModel> {
        return dataStore.getUserShipping().asLiveData()
    }

    fun getUserVerify(): LiveData<UserVerify> {
        return dataStore.getUserVerify().asLiveData()
    }

    suspend fun logout() {
        return dataStore.clearUser()
    }

    fun getStationRecommendation(mapRequest: MapRequest): LiveData<ApiResult<MapResponse>> {
        return liveData {
            try {
                emit(ApiResult.Loading)
                val response = apiServiceRecommendation.getStationRecommendation(mapRequest)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        emit(ApiResult.Success(responseBody))
                    }
                } else {
                    Log.e(TAG, "Error Maps Recommendation")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    Log.e(TAG, "Error: $errorResponse")

                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(
                        ApiResult.Error(
                            errorMessage.message ?: "Unknown Error in Maps Recommendation"
                        )
                    )
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    companion object {
        private const val TAG = "HomeRepository"

        @Volatile
        private var INSTANCE: HomeRepository? = null

        fun getInstance(
            apiService: ApiService,
            dataStore: UserPreferences,
            apiServiceRecommendation: ApiService
        ): HomeRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = HomeRepository(
                    apiService = apiService,
                    dataStore = dataStore,
                    apiServiceRecommendation
                )
                INSTANCE = instance
                instance
            }
        }
    }
}