package com.acme.process

import com.acme.display.CreditCardFormatter
import com.acme.model.CreditCardStatus
import com.acme.model.CreditCard
import com.acme.model.ProcessingOperation

object CreditCardProcessor {
    private val creditCardMap = mutableMapOf<String, CreditCard>()

    fun processLine(line: String) {
        when {
            line.startsWith(ProcessingOperation.ADD.operationName, true) -> processAdd(line)
            line.startsWith(ProcessingOperation.CHARGE.operationName, true) -> processCharge(line)
            line.startsWith(ProcessingOperation.CREDIT.operationName, true) -> processCredit(line)
            else -> return
        }
    }

    fun generateSummaryReport() = CreditCardFormatter.displayBalances(creditCardMap)

    private fun processAdd(line: String) {
        val cardHolder = getCardHolder(line)
        val cardNumber = getCardNumber(line)
        val cardLimit = getLineAmount(line)
        if (isValidCardNumber(cardNumber.toString())) {
            creditCardMap[cardHolder] = CreditCard(cardHolder, cardNumber, cardLimit)
        } else {
            creditCardMap[cardHolder] = CreditCard(cardHolder, cardNumber, cardLimit, CreditCardStatus.INVALID)
        }
    }

    private fun processCharge(line: String) {
        val cardHolder = getCardHolder(line)
        val creditCard = creditCardMap[cardHolder]
        if (shouldProcess(creditCard)) {
            val oldBalance = creditCard!!.balance
            if (oldBalance + getLineAmount(line) <= creditCard.limit) {
                val newBalance = oldBalance + getLineAmount(line)
                creditCardMap[cardHolder] = creditCard.copy(balance = newBalance)
            }
        }
    }

    private fun processCredit(line: String) {
        val cardHolder = getCardHolder(line)
        val creditCard = creditCardMap[cardHolder]
        if (shouldProcess(creditCard)) {
            val oldBalance = creditCard!!.balance
            val newBalance = oldBalance - getLineAmount(line)
            creditCardMap[cardHolder] = creditCard.copy(balance = newBalance)
        }
    }

    private fun shouldProcess(creditCard: CreditCard?): Boolean {
        return creditCard != null && creditCard.cardStatus == CreditCardStatus.VALID
    }

    private fun getCardHolder(line: String): String {
        val lineTokens = getLineItems(line)

        return lineTokens[1]
    }

    private fun getCardNumber(line: String): Long {
        val lineTokens = getLineItems(line)

        return lineTokens[2].toLong()
    }

    private fun getLineItems(line: String): List<String> {
        return line.split(Regex("\\s+"))
    }

    private fun getLineAmount(line: String): Long {
        val amount = when {
            line.startsWith(ProcessingOperation.ADD.operationName, true) ->
                getLineItems(line)[3].replace("$", "").toLong()

            line.startsWith(ProcessingOperation.CHARGE.operationName, true) || line.startsWith(ProcessingOperation.CREDIT.operationName, true) ->
                getLineItems(line)[2].replace("$", "").toLong()

            else -> 0L
        }

        return amount
    }

    private fun isValidCardNumber(cardNumber: String): Boolean {
        var checksum = 0

        for (i in cardNumber.length - 1 downTo 0 step 2) {
            checksum += cardNumber[i] - '0'
        }
        for (i in cardNumber.length - 2 downTo 0 step 2) {
            val n: Int = (cardNumber[i] - '0') * 2
            checksum += if (n > 9) n - 9 else n
        }

        return checksum % 10 == 0
    }
}

