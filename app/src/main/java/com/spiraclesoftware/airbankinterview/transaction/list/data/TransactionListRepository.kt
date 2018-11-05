package com.spiraclesoftware.airbankinterview.transaction.list.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spiraclesoftware.airbankinterview.api.ApiService
import com.spiraclesoftware.airbankinterview.transaction.list.data.dto.TransactionListResponse
import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.airbankinterview.transaction.list.domain.TransactionId
import com.spiraclesoftware.core.data.AppExecutors
import com.spiraclesoftware.core.data.NetworkBoundResource
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.testing.OpenForTesting
import javax.inject.Inject
import javax.inject.Singleton

@OpenForTesting
@Singleton
class TransactionListRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val cache: TransactionListCache
) {

    fun loadTransactionList() = loadTransactionList(TransactionListFilter(TransactionDirectionFilter.ALL))

    fun loadTransactionList(filter: TransactionListFilter): LiveData<Resource<List<Transaction>>> {
        return object : NetworkBoundResource<List<Transaction>, TransactionListResponse>(appExecutors) {

            override fun saveCallResult(data: TransactionListResponse) {
                cache.set(data.items)
            }

            override fun shouldFetch(data: List<Transaction>?): Boolean {
                return cache.isDirty || data == null
            }

            override fun loadFromCache(): LiveData<List<Transaction>> {
                return MutableLiveData<List<Transaction>>().apply {
                    value = cache.get()?.filter {
                        if (filter.transactionDirectionFilter == TransactionDirectionFilter.ALL)
                            true
                        else
                            filter.transactionDirectionFilter.mapsTo(it.direction)
                    }
                }
            }

            override fun createCall() = apiService.transactionList()

            override fun onFetchFailed() {
                cache.isDirty = true
            }
        }.asLiveData()
    }

    fun loadTransaction(transactionId: TransactionId): LiveData<Resource<Transaction>> {
        return object : NetworkBoundResource<Transaction, TransactionListResponse>(appExecutors) {

            override fun saveCallResult(data: TransactionListResponse) {
                cache.set(data.items)
            }

            override fun shouldFetch(data: Transaction?): Boolean {
                return cache.isDirty || data == null
            }

            override fun loadFromCache(): LiveData<Transaction> {
                return MutableLiveData<Transaction>().apply { value = cache.get(transactionId) }
            }

            override fun createCall() = apiService.transactionList()

            override fun onFetchFailed() {
                cache.isDirty = true
            }
        }.asLiveData()
    }
}