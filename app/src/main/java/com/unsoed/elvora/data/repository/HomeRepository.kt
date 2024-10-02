package com.unsoed.elvora.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.unsoed.elvora.data.response.home.Data
import com.unsoed.elvora.data.response.map.StationsItem
import com.unsoed.elvora.data.response.notification.DataItem
import com.unsoed.elvora.helper.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class HomeRepository(
    private val apiService: ApiService,
    private val dataStore: UserPreferences,
    private val apiServiceRecommendation: ApiService
) {
    private val _isDailyReminderActive = MutableLiveData<Event<Boolean>>()
    val isDailyReminderActive: LiveData<Event<Boolean>> = _isDailyReminderActive
    fun getUserSession(): LiveData<UserModel> {
        return dataStore.getUser().asLiveData()
    }

    fun getUserShipping(): LiveData<UserShippingModel> {
        return dataStore.getUserShipping().asLiveData()
    }

    fun getUserVerify(): LiveData<UserVerify> {
        return dataStore.getUserVerify().asLiveData()
    }

    fun getTierUser(): LiveData<Boolean> {
        return dataStore.getTierUser().asLiveData()
    }

    fun getReminderSubs() {
        val liveData = dataStore.getDailyReminderSubs().asLiveData()
        _isDailyReminderActive.value = Event(liveData.value ?: false)
    }

    suspend fun saveReminderSubs(isActive: Boolean) {
        return dataStore.saveDailyReminderSubs(isActive)
    }

    suspend fun logout() {
        return dataStore.clearUser()
    }

    fun getStationRecommendation(mapRequest: MapRequest): LiveData<ApiResult<List<StationsItem>>> {
        return liveData {
            try {
                emit(ApiResult.Loading)
                val response = apiServiceRecommendation.getStationRecommendation(mapRequest)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        emit(ApiResult.Success(responseBody.stations!!))
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

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): LiveData<ApiResult<String>> {
        return liveData {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getUser().firstOrNull()
            }

            try {
                emit(ApiResult.Loading)
                val response = apiService.changePassword(
                    token = "Bearer ${tokenUser?.token}",
                    currentPassword,
                    newPassword,
                    confirmPassword
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        emit(ApiResult.Success(responseBody.message!!))
                    }
                } else {
                    Log.e(TAG, "Error changePassword")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    Log.e(TAG, "Error: $errorResponse")

                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(
                        ApiResult.Error(
                            errorMessage.message ?: "Unknown Error in changePassword"
                        )
                    )
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun getDashboardData(): LiveData<ApiResult<Data>> {
        return liveData {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getUser().firstOrNull()
            }

            try {
                emit(ApiResult.Loading)
                val response = apiService.getDashboard(token = "Bearer ${tokenUser?.token}")
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d(TAG, "Response : ${response.toString()}")
                        Log.d(TAG,"Response Body : ${responseBody.data.toString()}")
                        emit(ApiResult.Success(responseBody.data!!))
                    }
                } else {
                    Log.e(TAG, "Error getDashboardData")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    Log.e(TAG, "Error: $errorResponse")

                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(
                        ApiResult.Error(
                            errorMessage.message ?: "Unknown Error in getDashboardData"
                        )
                    )
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }
    fun getNotificationData(): LiveData<ApiResult<List<DataItem>>> {
        return liveData {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getUser().firstOrNull()
            }

            try {
                emit(ApiResult.Loading)
                val response = apiService.getNotification(token = "Bearer ${tokenUser?.token}")
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d(TAG,"Response Body : ${responseBody.data.toString()}")
                        emit(ApiResult.Success(responseBody.data!!))
                    }
                } else {
                    Log.e(TAG, "Error getDashboardData")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    Log.e(TAG, "Error: $errorResponse")

                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(
                        ApiResult.Error(
                            errorMessage.message ?: "Unknown Error in getDashboardData"
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