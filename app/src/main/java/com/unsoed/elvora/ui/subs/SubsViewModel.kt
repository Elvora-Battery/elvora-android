package com.unsoed.elvora.ui.subs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.repository.SubsRepository
import com.unsoed.elvora.data.response.subs.Data

class SubsViewModel(private val subsRepository: SubsRepository): ViewModel() {
    fun getAllSubs(): LiveData<ApiResult<Data>> {
        return subsRepository.getAllSubs()
    }

    fun changeNameBattery(id: Int, name: String): LiveData<ApiResult<String>> {
        return subsRepository.changeBatteryName(id, name)
    }
}