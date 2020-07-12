package com.spiraclesoftware.androidsample.shared.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spiraclesoftware.androidsample.application.data.ApiService
import com.spiraclesoftware.androidsample.shared.data.dto.TransactionsResponseWrapper
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.domain.TransactionListFilter
import com.spiraclesoftware.androidsample.shared.domain.TransferDirectionFilter
import com.spiraclesoftware.core.data.*
import com.spiraclesoftware.core.testing.OpenForTesting

@OpenForTesting
class TransactionsRepository(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val listCache: AssociatedListCache<TransactionId, Transaction>
) {

    fun loadTransactionList() = loadTransactionList(
        TransactionListFilter(
            TransferDirectionFilter.ALL
        )
    )

    fun loadTransactionList(filter: TransactionListFilter): LiveData<Resource<List<Transaction>>> {
        return object :
            NetworkBoundResource<List<Transaction>, TransactionsResponseWrapper>(appExecutors) {

            override fun saveCallResult(data: TransactionsResponseWrapper) {
                listCache.set(data.items)
            }

            override fun shouldFetch(data: List<Transaction>?): Boolean {
                return listCache.isDirty || data == null
            }

            override fun loadFromCache(): LiveData<List<Transaction>> {
                val cachedData = listCache.get()

                return if (cachedData == null) {
                    AbsentLiveData.create()
                } else {
                    MutableLiveData<List<Transaction>>().apply {
                        value = cachedData
                            .filter {
                                if (filter.transferDirectionFilter == TransferDirectionFilter.ALL)
                                    true
                                else
                                    filter.transferDirectionFilter.mapsTo(it.transferDirection)
                            }
                    }
                }
            }

            override fun createCall() = apiService.transactionListOld()

            override fun onFetchFailed() {
                listCache.isDirty = true
            }
        }.asLiveData()
    }

    fun dirtyCache() {
        listCache.isDirty = true
    }
}