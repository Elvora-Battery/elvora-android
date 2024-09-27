package com.unsoed.elvora.ui.subs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.repository.SubsRepository
import com.unsoed.elvora.data.response.active.DataItem
import com.unsoed.elvora.data.response.getSubs.Data

class SubsViewModel(private val subsRepository: SubsRepository): ViewModel() {
    fun getAllSubs(): LiveData<ApiResult<Data>> {
        return subsRepository.getAllSubs()
    }
    fun getActiveSubs(): LiveData<ApiResult<List<DataItem>>> {
        return subsRepository.getActiveSubs()
    }

    fun getTransactionById(id: Int): LiveData<ApiResult<com.unsoed.elvora.data.response.transactionId.Data>> {
        return subsRepository.getTransactionById(id)
    }

    fun changeNameBattery(id: Int, name: String): LiveData<ApiResult<String>> {
        return subsRepository.changeBatteryName(id, name)
    }
}