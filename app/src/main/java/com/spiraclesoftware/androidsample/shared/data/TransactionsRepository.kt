package com.spiraclesoftware.androidsample.shared.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spiraclesoftware.androidsample.application.data.ApiService
import com.spiraclesoftware.androidsample.shared.data.dto.TransactionListResponse
import com.spiraclesoftware.androidsample.shared.domain.*
import com.spiraclesoftware.core.data.*
import com.spiraclesoftware.core.testing.OpenForTesting
import javax.inject.Inject
import javax.inject.Singleton

@OpenForTesting
@Singleton
class TransactionsRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val listCache: AssociatedListCache<TransactionId, Transaction>,
    private val detailCache: AssociatedItemCache<TransactionId, TransactionDetail>
) {

    fun loadTransactionList() = loadTransactionList(
        TransactionListFilter(
            TransactionDirectionFilter.ALL
        )
    )

    fun loadTransactionList(filter: TransactionListFilter): LiveData<Resource<List<Transaction>>> {
        return object : NetworkBoundResource<List<Transaction>, TransactionListResponse>(appExecutors) {

            override fun saveCallResult(data: TransactionListResponse) {
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
                                if (filter.transactionDirectionFilter == TransactionDirectionFilter.ALL)
                                    true
                                else
                                    filter.transactionDirectionFilter.mapsTo(it.direction)
                            }
                    }
                }
            }

            override fun createCall() = apiService.transactionList()

            override fun onFetchFailed() {
                listCache.isDirty = true
            }
        }.asLiveData()
    }

    fun loadTransaction(transactionId: TransactionId): LiveData<Resource<Transaction>> {
        return object : NetworkBoundResource<Transaction, TransactionListResponse>(appExecutors) {

            override fun saveCallResult(data: TransactionListResponse) {
                listCache.set(data.items)
            }

            override fun shouldFetch(data: Transaction?): Boolean {
                return listCache.isDirty || data == null
            }

            override fun loadFromCache(): LiveData<Transaction> {
                val cachedData = listCache.get(transactionId)

                return if (cachedData == null) {
                    AbsentLiveData.create()
                } else {
                    MutableLiveData<Transaction>().apply { value = cachedData }
                }
            }

            override fun createCall() = apiService.transactionList()

            override fun onFetchFailed() {
                listCache.isDirty = true
            }
        }.asLiveData()
    }

    fun loadTransactionDetail(transactionId: TransactionId): LiveData<Resource<TransactionDetail>> {
        return object : NetworkBoundResource<TransactionDetail, TransactionDetail>(appExecutors) {

            override fun saveCallResult(data: TransactionDetail) {
                detailCache.set(transactionId, data)
            }

            override fun shouldFetch(data: TransactionDetail?): Boolean {
                return detailCache.isDirty || data == null
            }

            override fun loadFromCache(): LiveData<TransactionDetail> {
                val cachedData = detailCache.get(transactionId)

                return if (cachedData == null) {
                    AbsentLiveData.create()
                } else {
                    MutableLiveData<TransactionDetail>().apply { value = cachedData }
                }
            }

            override fun createCall() = apiService.transactionDetail(transactionId.value)

            override fun onFetchFailed() {
                detailCache.isDirty = true
            }
        }.asLiveData()
    }
}