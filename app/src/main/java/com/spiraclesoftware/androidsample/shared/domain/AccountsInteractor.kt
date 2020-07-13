package com.spiraclesoftware.androidsample.shared.domain

import com.spiraclesoftware.androidsample.shared.data.disk.DiskDataSource

class AccountsInteractor(
    private val diskDataSource: DiskDataSource
) {

    fun getAccount(): Account? {
        return diskDataSource.getAccount()
    }

}
