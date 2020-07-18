package com.spiraclesoftware.androidsample.domain.policy

import com.spiraclesoftware.androidsample.domain.model.Money
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionStatus
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode
import java.math.BigDecimal
import java.util.*

class TransactionsPolicy(
    private val currencyConverter: CurrencyConverter
) {

    /** Put here the methods that don't need injected dependencies, to simplify calling them. */
    companion object {

        fun isSuccessful(transaction: Transaction): Boolean {
            val successful = transaction.statusCode == TransactionStatusCode.SUCCESSFUL
            val completed = transaction.status == TransactionStatus.COMPLETED
            return successful && completed
        }

        fun contributesToBalance(transaction: Transaction): Boolean {
            return isSuccessful(transaction)
        }

    }

    suspend fun getContributionToBalance(
        transactions: List<Transaction>,
        accountCurrency: Currency
    ): Money {
        val initial = Money(BigDecimal.ZERO, accountCurrency)

        return transactions.fold(initial) { acc, transaction ->
            acc.add(getContributionToBalance(transaction, accountCurrency).amount)
        }
    }

    /**
     * Calculates how much a transaction contributes to the account balance.
     * @return contributed amount in account currency
     */
    suspend fun getContributionToBalance(
        transaction: Transaction,
        accountCurrency: Currency
    ): Money {
        if (!contributesToBalance(transaction)) {
            return Money(BigDecimal.ZERO, accountCurrency)
        }

        return currencyConverter.convert(transaction.signedMoney, accountCurrency)
    }

}