package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.entity.Account
import com.spiraclesoftware.androidsample.domain.entity.Money
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.service.CurrencyConverter
import java.math.BigDecimal

class AccountsInteractor(
    private val localDataSource: LocalDataSource,
    private val currencyConverter: CurrencyConverter
) {

    fun getAccount(): Account {
        return localDataSource.getAccount()
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
