package com.unsoed.elvora.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.repository.AuthRepository
import com.unsoed.elvora.data.response.Data


class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun createAccount(email: String, name: String): LiveData<ApiResult<Data>> {
        return authRepository.createAccount(email, name)
    }

    fun verifyOTP(email: String, otp: String): LiveData<ApiResult<String>> {
        return authRepository.verifyOTP(email, otp)
    }

    fun setPassword(email: String, password: String, confirmationPassword: String): LiveData<ApiResult<String>> {
        return authRepository.setPassword(email, password, confirmationPassword)
    }

    fun login(email: String, password: String): LiveData<ApiResult<String>> {
        return authRepository.login(email, password)
    }

    fun getUserSession(): LiveData<UserModel> {
        return authRepository.getUserSession()
    }
}