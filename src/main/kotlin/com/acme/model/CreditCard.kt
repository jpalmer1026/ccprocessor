package com.acme.model

data class CreditCard(
        val name: String,
        val cardNumber: Long,
        val limit: Long,
        val cardStatus: CreditCardStatus = CreditCardStatus.VALID,
        val balance: Long = 0L
)
