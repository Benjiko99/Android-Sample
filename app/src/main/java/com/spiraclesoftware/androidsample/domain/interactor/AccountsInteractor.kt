package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.data.memory.MemoryDataSource
import com.spiraclesoftware.androidsample.domain.model.Account
import com.spiraclesoftware.androidsample.domain.model.Money
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.policy.CurrencyConverter
import java.math.BigDecimal

class AccountsInteractor(
    private val memoryDataSource: MemoryDataSource,
    private val currencyConverter: CurrencyConverter
) {

    fun getAccount(): Account {
        return memoryDataSource.getAccount()
    }

    /**
     * Calculates how much transactions contribute to the account balance.
     * @return contributed amount in account currency
     */
    suspend fun getContributionToBalance(transactions: List<Transaction>): Money {
        val initial = Money(BigDecimal.ZERO, getAccount().currency)

        return transactions.fold(initial) { acc, transaction ->
            acc.add(getContributionToBalance(transaction).amount)
        }
    }

    /**
     * Calculates how much a transaction contributes to the account balance.
     * @return contributed amount in account currency
     */
    private suspend fun getContributionToBalance(transaction: Transaction): Money {
        if (!transaction.contributesToAccountBalance()) {
            return Money(BigDecimal.ZERO, getAccount().currency)
        }

        return currencyConverter.convert(transaction.signedMoney, getAccount().currency)
    }

}
