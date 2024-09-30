package com.unsoed.elvora.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.MapRequest
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.UserShippingModel
import com.unsoed.elvora.data.UserVerify
import com.unsoed.elvora.data.repository.HomeRepository
import com.unsoed.elvora.data.response.home.Data
import com.unsoed.elvora.data.response.map.StationsItem
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    fun getUserSession(): LiveData<UserModel> {
        return homeRepository.getUserSession()
    }

    fun getUserShipping(): LiveData<UserShippingModel> {
        return homeRepository.getUserShipping()
    }

    fun getUserVerify(): LiveData<UserVerify> {
        return homeRepository.getUserVerify()
    }
    fun getTierUser(): LiveData<Boolean> {
        return homeRepository.getTierUser()
    }

    fun getReminderSubs(): LiveData<Boolean> {
        return homeRepository.getReminderSubs()
    }


    fun setReminderSubs(isEnabled: Boolean) {
        viewModelScope.launch {
            homeRepository.saveReminderSubs(isEnabled)
        }
    }

    fun getStationRecommendation(mapRequest: MapRequest): LiveData<ApiResult<List<StationsItem>>> {
        return homeRepository.getStationRecommendation(mapRequest)
    }

    fun getDashboardData(): LiveData<ApiResult<Data>> {
        return homeRepository.getDashboardData()
    }
    fun changePassword(currentPass: String, newPass: String, confirmPass: String): LiveData<ApiResult<String>> {
        return homeRepository.changePassword(currentPass, newPass, confirmPass)
    }

    fun logout() {
        viewModelScope.launch {
            homeRepository.logout()
        }
    }
}