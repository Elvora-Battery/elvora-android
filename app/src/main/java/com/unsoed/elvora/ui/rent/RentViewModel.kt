package com.unsoed.elvora.ui.rent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.UserShippingModel
import com.unsoed.elvora.data.repository.RentRepository
import com.unsoed.elvora.data.response.PaidTransactionResponse
import com.unsoed.elvora.data.response.getSubs.AllSubsriptionsItem
import com.unsoed.elvora.data.response.new.Data
import com.unsoed.elvora.data.response.verify.KtpData
import okhttp3.MultipartBody

class RentViewModel(private val rentRepository: RentRepository): ViewModel() {
    fun getUserSession(): LiveData<UserModel> {
        return rentRepository.getUserSession()
    }

    fun getUserShipping(): LiveData<UserShippingModel> {
        return rentRepository.getUserShipping()
    }

    fun getTierUser(): LiveData<Boolean> {
        return rentRepository.getTierUser()
    }

    fun createShippingUser(shippingModel: UserShippingModel): LiveData<ApiResult<String>> {
        return rentRepository.createShipping(shippingModel)
    }

    fun newTransaction(idRent: String): LiveData<ApiResult<Data>> {
        return rentRepository.newTransaction(idRent)
    }

    fun paidTransaction(idTransaction: String): LiveData<ApiResult<PaidTransactionResponse>> {
        return rentRepository.paidTransaction(idTransaction)
    }

    fun uploadKtp(file: MultipartBody.Part): LiveData<ApiResult<KtpData>> {
        return rentRepository.uploadKtp(file)
    }

    fun verificationKtp(nik: String, name: String, date: String): LiveData<ApiResult<String>> {
        return rentRepository.verificationKtp(nik, name, date)
    }

    fun publishToken(token: String): LiveData<ApiResult<String>> {
        return rentRepository.publishToken(token)
    }
    fun getAllTransaction(): LiveData<ApiResult<List<AllSubsriptionsItem>>> {
        return rentRepository.getAllTransaction()
    }
}