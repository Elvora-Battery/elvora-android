package com.unsoed.elvora.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.local.UserPreferences
import com.unsoed.elvora.data.network.ApiService
import com.unsoed.elvora.data.response.Data

class AuthRepository(
    private val apiService: ApiService,
    private val dataStore: UserPreferences
) {
    fun createAccount(email: String, name: String): LiveData<ApiResult<Data>> {
        return liveData {
            emit(ApiResult.Loading)
            val response = apiService.createAccount(email, name)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(ApiResult.Success(responseBody.data!!))
                }
            } else {
                emit(ApiResult.Error(response.body()?.message!!))
                Log.e(TAG, "Error Register Account")
            }
        }
    }

    fun verifyOTP(email: String, otp: String): LiveData<ApiResult<String>> {
        return liveData {
            emit(ApiResult.Loading)
            val response = apiService.verifyOTP(email, otp)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(ApiResult.Success(responseBody.message!!))
                }
            } else {
                emit(ApiResult.Error(response.body()?.message!!))
                Log.e(TAG, "Error Verify OTP")
            }
        }
    }

    fun setPassword(
        email: String,
        password: String,
        confirmationPassword: String
    ): LiveData<ApiResult<String>> {
        return liveData {
            emit(ApiResult.Loading)
            val response = apiService.setPassword(email, password, confirmationPassword)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(ApiResult.Success(responseBody.message!!))
                }
            } else {
                emit(ApiResult.Error(response.body()?.message!!))
                Log.e(TAG, "Error Set Password")
            }
        }
    }

    fun getUserSession(): LiveData<UserModel> {
        return dataStore.getUser().asLiveData()
    }

    fun login(email: String, password: String): LiveData<ApiResult<String>> {
        return liveData {
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
                            premium = false
                        )
                    )
                }
            } else {
                emit(ApiResult.Error(response.body()?.message!!))
                Log.e(TAG, "Error Login")
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