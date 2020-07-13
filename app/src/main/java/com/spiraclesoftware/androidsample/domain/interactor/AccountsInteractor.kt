package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.domain.model.Account

class AccountsInteractor(
    private val diskDataSource: DiskDataSource
) {

    fun getAccount(): Account {
        return diskDataSource.getAccount()
    }

}
