package com.spiraclesoftware.androidsample.shared.domain

enum class TransferDirection {
    INCOMING, OUTGOING;

    val symbol: Char
        get() = when (this) {
            INCOMING -> '+'
            OUTGOING -> '-'
        }
}