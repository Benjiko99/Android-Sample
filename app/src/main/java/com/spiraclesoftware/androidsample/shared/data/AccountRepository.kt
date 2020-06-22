package com.spiraclesoftware.androidsample.shared.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spiraclesoftware.androidsample.shared.domain.Account
import com.spiraclesoftware.core.testing.OpenForTesting
import java.util.*

@OpenForTesting
class AccountRepository {

    private val account: Account = Account(Currency.getInstance("EUR"))

    fun getAccount(): LiveData<Account> {
        return MutableLiveData<Account>().apply {
            value = account
        }
    }
}