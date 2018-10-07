package com.acme.display

import com.acme.model.CreditCard
import com.acme.model.CreditCardStatus

object CreditCardFormatter {
    fun displayBalances(creditCardMap: Map<String, CreditCard> ) {
        for (creditCard in creditCardMap.values.sortedBy { it.name }) {
            print("${creditCard.name}: " )
            if (creditCard.cardStatus == CreditCardStatus.VALID) {
                println("$${creditCard.balance}")
            } else {
                println("error")
            }
        }
    }
}