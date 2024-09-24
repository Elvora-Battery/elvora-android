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
import com.unsoed.elvora.data.response.map.MapResponse
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

    fun getStationRecommendation(mapRequest: MapRequest): LiveData<ApiResult<MapResponse>> {
        return homeRepository.getStationRecommendation(mapRequest)
    }

    fun logout() {
        viewModelScope.launch {
            homeRepository.logout()
        }
    }
}