package com.unsoed.elvora.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.UserShippingModel
import com.unsoed.elvora.data.UserVerify
import com.unsoed.elvora.data.local.UserPreferences
import com.unsoed.elvora.data.network.ApiService
import com.unsoed.elvora.data.response.CommonResponse
import com.unsoed.elvora.data.response.PaidTransactionResponse
import com.unsoed.elvora.data.response.new.Data
import com.unsoed.elvora.data.response.verify.KtpData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class RentRepository(private val apiService: ApiService, private val dataStore: UserPreferences, private val apiServiceScan: ApiService) {

    fun createShipping(shippingModel: UserShippingModel): LiveData<ApiResult<String>> {
        return liveData {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getUser().firstOrNull()
            }
            try {
                emit(ApiResult.Loading)
                val response = apiService.createShipping(
                    token = "Bearer ${tokenUser?.token}",
                    userId = "1",
                    name = shippingModel.name,
                    phone = shippingModel.telephoneNumber,
                    streetName = shippingModel.street,
                    village = shippingModel.village,
                    fullAddress = shippingModel.address
                )
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        emit(ApiResult.Success(responseBody.message!!))
                        dataStore.saveUserShipping(shippingModel)
                    }
                } else {
                    Log.e(TAG, "Error Create Shipping")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message ?: "Unknown Error in Create Shipping"))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun uploadKtp(file: MultipartBody.Part): LiveData<ApiResult<KtpData>> {
        return liveData {
            try {
                emit(ApiResult.Loading)
                val response = apiServiceScan.uploadImageKtp(file)
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        emit(ApiResult.Success(responseBody.ktpData!!))
                    }
                } else {
                    Log.e(TAG, "Error Upload KTP")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message ?: "Unknown Error in Upload KTP"))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun verificationKtp(nik: String, name: String, date: String): LiveData<ApiResult<String>> {
        return liveData {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getUser().firstOrNull()
            }

            try {
                emit(ApiResult.Loading)
                val response = apiService.verificationKtp(
                    token = "Bearer ${tokenUser?.token}",
                    nik = nik,
                    nama = name,
                    tanggal_lahir = date
                )
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        dataStore.saveUserPremium(true)
                        dataStore.saveUserVerify(
                            UserVerify(
                                name = name,
                                nik = nik,
                                date = date
                            )
                        )
                        emit(ApiResult.Success(responseBody.message ?: ""))
                    }
                } else {
                    Log.e(TAG, "Error Verification KTP")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message ?: "Unknown Error in Verification KTP"))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun newTransaction(idRent: String): LiveData<ApiResult<Data>> {
        return liveData {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getUser().firstOrNull()
            }
            try {
                emit(ApiResult.Loading)
                val response = apiService.createNewTransaction(
                    token = "Bearer ${tokenUser?.token}",
                    rentTypeId = idRent
                )
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        emit(ApiResult.Success(responseBody.data!!))
                    }
                } else {
                    Log.e(TAG, "Error New Transaction")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message ?: "Unknown Error in New Transaction"))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun publishToken(token: String): LiveData<ApiResult<String>> {
        return liveData {
            try {
                emit(ApiResult.Loading)
                val response = apiService.publishToken(
                    token = token
                )
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        emit(ApiResult.Success(responseBody.message!!))
                    }
                } else {
                    Log.e(TAG, "Error Publish Token")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message ?: "Unknown Error in Publish Token"))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun paidTransaction(idTransaction: String): LiveData<ApiResult<PaidTransactionResponse>> {
        return liveData {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getUser().firstOrNull()
            }

            try {
                emit(ApiResult.Loading)
                val response = apiService.transactionPaid(
                    token = "Bearer ${tokenUser?.token}",
                    id = idTransaction
                )
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        emit(ApiResult.Success(responseBody))
                    }
                } else {
                    Log.e(TAG, "Error Paid Transaction")
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message ?: "Unknown Error in Paid Transaction"))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun getUserSession(): LiveData<UserModel> {
        return dataStore.getUser().asLiveData()
    }

    fun getTierUser(): LiveData<Boolean> {
        return dataStore.getTierUser().asLiveData()
    }

    fun getUserShipping(): LiveData<UserShippingModel> {
        return dataStore.getUserShipping().asLiveData()
    }

    companion object {
        private const val TAG = "RentRepository"
        @Volatile
        private var INSTANCE: RentRepository? = null

        fun getInstance(apiService: ApiService, dataStore: UserPreferences, apiServiceScan: ApiService): RentRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = RentRepository(apiService = apiService, dataStore = dataStore, apiServiceScan)
                INSTANCE = instance
                instance
            }
        }
    }
}