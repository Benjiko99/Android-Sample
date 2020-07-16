package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.data.memory.MemoryDataSource
import com.spiraclesoftware.androidsample.domain.model.Account

class AccountsInteractor(
    private val memoryDataSource: MemoryDataSource
) {

    fun getAccount(): Account {
        return memoryDataSource.getAccount()
    }

}
