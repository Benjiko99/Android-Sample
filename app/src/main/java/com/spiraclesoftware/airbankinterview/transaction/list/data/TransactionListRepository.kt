package com.spiraclesoftware.airbankinterview.transaction.list.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spiraclesoftware.airbankinterview.api.ApiService
import com.spiraclesoftware.airbankinterview.transaction.list.data.dto.TransactionListResponse
import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.core.data.AppExecutors
import com.spiraclesoftware.core.data.NetworkBoundResource
import com.spiraclesoftware.core.data.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionListRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val cache: TransactionListCache
) {

    fun loadTransactionList(): LiveData<Resource<List<Transaction>>> {
        return object : NetworkBoundResource<List<Transaction>, TransactionListResponse>(appExecutors) {

            override fun saveCallResult(data: TransactionListResponse) {
                cache.set(data.items)
            }

            override fun shouldFetch(data: List<Transaction>?): Boolean {
                return cache.cacheIsDirty || data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Transaction>> {
                return MutableLiveData<List<Transaction>>().apply { value = cache.get() }
            }

            override fun createCall() = apiService.transactionList()

            override fun onFetchFailed() {
                cache.cacheIsDirty = true
            }
        }.asLiveData()
    }
}