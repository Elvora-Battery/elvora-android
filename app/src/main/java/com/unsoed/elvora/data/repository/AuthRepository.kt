package com.unsoed.elvora.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.local.UserPreferences
import com.unsoed.elvora.data.network.ApiService
import com.unsoed.elvora.data.response.CommonResponse
import com.unsoed.elvora.data.response.Data

class AuthRepository(
    private val apiService: ApiService,
    private val dataStore: UserPreferences
) {
    fun createAccount(email: String, name: String): LiveData<ApiResult<Data>> {
        return liveData {
            try {
                emit(ApiResult.Loading)
                val response = apiService.createAccount(email, name)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        emit(ApiResult.Success(responseBody.data!!))
                    }
                } else {
                    Log.e(TAG, "Error createAccount")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    Log.e(TAG, "Error: $errorResponse")

                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(
                        ApiResult.Error(
                            errorMessage.message ?: "Unknown Error in createAccount"
                        )
                    )
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun verifyOTP(email: String, otp: String): LiveData<ApiResult<String>> {
        return liveData {
            try {
                emit(ApiResult.Loading)
                val response = apiService.verifyOTP(email, otp)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        emit(ApiResult.Success(responseBody.message!!))
                    }
                } else {
                    Log.e(TAG, "Error verifyOTP")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    Log.e(TAG, "Error: $errorResponse")

                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(
                        ApiResult.Error(
                            errorMessage.message ?: "Unknown Error in verifyOTP"
                        )
                    )
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun setPassword(
        email: String,
        password: String,
        confirmationPassword: String
    ): LiveData<ApiResult<String>> {
        return liveData {
            try {
                emit(ApiResult.Loading)
                val response = apiService.setPassword(email, password, confirmationPassword)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        emit(ApiResult.Success(responseBody.message!!))
                    }
                } else {
                    Log.e(TAG, "Error verifyOTP")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    Log.e(TAG, "Error: $errorResponse")

                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(
                        ApiResult.Error(
                            errorMessage.message ?: "Unknown Error in verifyOTP"
                        )
                    )
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun getUserSession(): LiveData<UserModel> {
        return dataStore.getUser().asLiveData()
    }

    fun login(email: String, password: String): LiveData<ApiResult<String>> {
        return liveData {
            try {
                emit(ApiResult.Loading)
                val response = apiService.login(email, password)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        emit(ApiResult.Success(responseBody.message!!))
                        dataStore.saveUser(
                            UserModel(
                                email = email,
                                fullName = responseBody.data?.user?.name!!,
                                token = responseBody.data.token!!,
                            )
                        )
                        dataStore.saveUserPremium(responseBody.data.user.verify!!)
                    }
                } else {
                    Log.e(TAG, "Error login")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    Log.e(TAG, "Error: $errorResponse")

                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(
                        ApiResult.Error(
                            errorMessage.message ?: "Unknown Error in login"
                        )
                    )
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    companion object {
        private const val TAG = "AuthRepository"

        @Volatile
        private var INSTANCE: AuthRepository? = null

        fun getInstance(apiService: ApiService, dataStore: UserPreferences): AuthRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthRepository(apiService = apiService, dataStore = dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}